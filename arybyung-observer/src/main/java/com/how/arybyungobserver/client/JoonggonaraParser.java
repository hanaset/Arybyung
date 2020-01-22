package com.how.arybyungobserver.client;

import com.how.arybyungobserver.service.FilteringWordService;
import com.how.arybyungobserver.utils.DriverUtil;
import com.how.muchcommon.entity.ArticleEntity;
import com.how.muchcommon.model.type.ArticleState;
import com.how.muchcommon.repository.ArticleRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JoonggonaraParser {

    private final ArticleRepository articleRepository;
    private final FilteringWordService filteringWordService;
    private Map<String, String> cookieMaps;
    private Integer count = 0;

    public JoonggonaraParser(ArticleRepository articleRepository,
                             FilteringWordService filteringWordService) {
        this.articleRepository = articleRepository;
        this.filteringWordService = filteringWordService;
    }

    public Long getRecentArticleId() {

        if(getCount() > 0) {
            return 0L;
        }

        try {
            Document document = Jsoup.connect(ParserConstants.JOONGGONARA_POST_LIST)
                    .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .header(HttpHeaders.ACCEPT_CHARSET, "utf-8")
                    .cookies(cookieMaps)
                    .get();

            List<Element> elements = document.select("[class=article]");
            String maxArticleId = elements.stream().map(element -> {
                String articleId = Arrays.asList(element.attr("href").split("&")).stream().filter(s -> s.contains("articleid")).map(s -> s.replaceAll("[^0-9]", "")).findFirst().get();

                return articleId;
            }).max(String::compareToIgnoreCase).orElse("0");

            return Long.parseLong(maxArticleId);
        } catch (IOException e) {
            log.error("JoonggoNARA 과다 호출로 인한 일시 제한");
            return 0L;
        }
    }

    @Async(value = "joonggonaraTaskExecutor")
    public void getArticle(Long articleId) {

        String url = ParserConstants.JOONGGONARA_POST + articleId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        if(getCount() > 0) {
            return;
        }

        try {
            Document document = Jsoup.connect(url)
                    .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .header(HttpHeaders.ACCEPT_CHARSET, "utf-8")
                    .cookies(cookieMaps)
                    .get();

            Element login = document.selectFirst("iframe"); // 게시글이 정상적으로 파싱되어있는지에 대한 확인 기준

            if (!login.attr("title").equals("로그인영역")) { // cookie가 유효한지 확인 (정상적으로 파싱 완료)

                Element subjectElement = document.selectFirst("[class=b m-tcol-c]");

                if (filteringWordService.stringFilter(subjectElement.text())) {
                    return;
                }

                if (!subjectElement.text().contains("공식앱")) {

                    Element imageElement = document.selectFirst("[class=imgs border-sub]");

                    Element costElement = document.selectFirst("[class=cost]");
                    Long price = Long.parseLong(costElement.text().replaceAll("[^0-9]", ""));

                    Elements contentElements1 = document.select("[class=NHN_Writeform_Main]");
                    Elements contentElements2 = document.selectFirst("[class=tbody m-tcol-c]").select("p").select("span");

                    String content = contentElements1.text() + "\n" + contentElements2.text();

                    if (content.length() > 2000 || filteringWordService.stringFilter(content)) {
                        return;
                    }

                    Element dateElement = document.selectFirst("[class=m-tcol-c date]");
                    Element stateElement = document.selectFirst("em");

                    ArticleEntity entity = ArticleEntity.builder()
                            .articleId(articleId)
                            .subject(subjectElement.text())
                            .image(imageElement.attr("src"))
                            .price(price)
                            .content(content)
                            .postingDtime(LocalDateTime.parse(dateElement.text(), formatter).atZone(ZoneId.of("Asia/Seoul")))
                            .site("joonggonara")
                            .state(stateElement.attr("aria-label").equals("판매") ? ArticleState.S : ArticleState.C)
                            .url(url)
                            .build();

                    articleRepository.save(entity);
                } else {
                    Element bodyElement = document.selectFirst("[class=tbody m-tcol-c]");
                    Element imgElement = bodyElement.selectFirst("img");
                    Element dateElement = document.selectFirst("[class=m-tcol-c date]");
                    Long price = Long.parseLong(bodyElement.selectFirst("[color=#FF6C00]").text().replaceAll("[^0-9]", ""));

                    if (bodyElement.text().length() > 2000 || filteringWordService.stringFilter(bodyElement.text())) {
                        return;
                    }

                    ArticleEntity entity = ArticleEntity.builder()
                            .articleId(articleId)
                            .subject(subjectElement.text())
                            .image(imgElement.attr("img"))
                            .price(price)
                            .content(bodyElement.text())
                            .postingDtime(LocalDateTime.parse(dateElement.text(), formatter).atZone(ZoneId.of("Asia/Seoul")))
                            .site("joongonara")
                            .state(ArticleState.S)
                            .url(url)
                            .build();

                    articleRepository.save(entity);
                }

            } else { // 쿠키를 재발급 받아야할 때 (비정상적으로 파싱)
                naverLogin();
            }
        } catch (NullPointerException e) {
//            log.error("{} : Not found", url, e.getMessage());
        } catch (HttpStatusException e) {
            setCount(getCount() + 1);
            log.error("Joonggonara 과다 호출 : {} / {}", e.getUrl(), e.getStatusCode());
        } catch (InterruptedException e) {
            log.error("Naver Login Interrupt Exception : {}", e.getMessage());
        } catch (IOException e) {
            log.error("get IOException : {}", e.getMessage());
        }
    }

    @PostConstruct
    public void naverLogin() throws InterruptedException {

        System.setProperty("webdriver.gecko.driver", ParserConstants.DRIVER_PATH);
//        System.setProperty("webdriver.gecko.driver", ParserConstants.TEST_DRIVER_PATH); //테스트코드
        System.setProperty("java.awt.headless", "false");

        WebDriver driver = new FirefoxDriver();
        driver.get(ParserConstants.NAVER_LOGIN);

        DriverUtil.clipboardCopy("b183523", "//*[@id=\"id\"]", driver);
        DriverUtil.clipboardCopy("thswjdqls56@!", "//*[@id=\"pw\"]", driver);

        driver.findElement(By.xpath("//*[@id=\"frmNIDLogin\"]/fieldset/input")).click();

        Set<Cookie> cookies = driver.manage().getCookies();
        this.cookieMaps = cookies.stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
    }

    @Synchronized
    public Integer getCount() {
        return count;
    }

    @Synchronized
    public void setCount(Integer count) {
        this.count = count;
    }
}
