package com.hanaset.arybyungobserver.client;

import com.hanaset.arybyungcommon.entity.ArticleEntity;
import com.hanaset.arybyungcommon.model.type.ArticleState;
import com.hanaset.arybyungcommon.repository.ArticleRepository;
import com.hanaset.arybyungobserver.utils.DriverUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
    private Map<String, String> cookieMaps;

    public JoonggonaraParser(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Long getRecentArticleId() throws IOException {
        Document document = Jsoup.connect(ParserConstants.JOONGGONARA_POST_LIST)
                .cookies(cookieMaps)
                .get();

        List<Element> elements = document.select("[class=article]");
        String maxArticleId = elements.stream().map(element -> {
            String articleId = Arrays.asList(element.attr("href").split("&")).stream().filter(s -> s.contains("articleid")).map(s -> s.replaceAll("[^0-9]", "")).findFirst().get();

            return articleId;
        }).max(String::compareToIgnoreCase).orElse("0");

        return Long.parseLong(maxArticleId);
    }

    @Async(value = "joonggonaraTaskExecutor")
    public void getArticle(Long articleId) throws Exception {


        String url = ParserConstants.JOONGGONARA_POST + articleId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        try {
            Document document = Jsoup.connect(url)
                    .cookies(cookieMaps)
                    .get();

            Element login = document.selectFirst("iframe"); // 게시글이 정상적으로 파싱되어있는지에 대한 확인 기준

            if (!login.attr("title").equals("로그인영역")) { // cookie가 유효한지 확인 (정상적으로 파싱 완료)

                Element subjectElement = document.selectFirst("[class=b m-tcol-c]");
//            System.out.println(subjectElement.text());

                Element imageElement = document.selectFirst("[class=imgs border-sub]");
//            System.out.println(imageElement.attr("src"));

                Element costElement = document.selectFirst("[class=cost]");
//            System.out.println(costElement.text());
                Long price = Long.parseLong(costElement.text().replaceAll("[^0-9]", ""));

                Element contentElement = document.selectFirst("[class=tbody m-tcol-c]");
//            System.out.println(contentElement.text());

                Element dateElement = document.selectFirst("[class=m-tcol-c date]");
//            System.out.println(dateElement.text());

                Element stateElement = document.selectFirst("em");
//            System.out.println(stateElement.attr("aria-label"));

                ArticleEntity entity = ArticleEntity.builder()
                        .articleId(articleId)
                        .subject(subjectElement.text())
                        .image(imageElement.attr("src"))
                        .price(price)
                        .content(contentElement.text().length() > 4000 ? null : contentElement.text())
                        .postingDtime(LocalDateTime.parse(dateElement.text(), formatter).atZone(ZoneId.of("Asia/Seoul")))
                        .site("joongonara")
                        .state(stateElement.attr("aria-label").equals("판매") ? ArticleState.S : ArticleState.C)
                        .url(url)
                        .build();

                articleRepository.save(entity);

            } else { // 쿠키를 재발급 받아야할 때 (비정상적으로 파싱)
                naverLogin();
            }
        } catch (NullPointerException e) {
            log.error("{} : {}", url, e.getMessage());
        }
    }

    //    @PostConstruct
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
}
