package com.how.arybyungobserver.client;

import com.how.arybyungobserver.service.FilteringWordService;
import com.how.muchcommon.entity.ArticleEntity;
import com.how.muchcommon.model.type.ArticleState;
import com.how.muchcommon.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;

@Slf4j
@Component
public class DanggnMarketParser {

    private final ArticleRepository articleRepository;
    private final FilteringWordService filteringWordService;

    public DanggnMarketParser(ArticleRepository articleRepository,
                              FilteringWordService filteringWordService) {
        this.articleRepository = articleRepository;
        this.filteringWordService = filteringWordService;
    }

    public Long getRecentArticleId() throws IOException {
        Document document = Jsoup.connect(ParserConstants.DANGGNMARKET_POST_LIST)
                .get();

        List<Element> elements = document.select("[data-event-label]");
        Long maxArticleId = elements.stream().map(element -> Long.parseLong(element.attr("data-event-label"))).max(Long::compareTo).orElse(0L);

        return maxArticleId;
    }

    @Async(value = "danggnMarketTaskExecutor")
    public void getArticle(Long articleId) throws IOException {

        String url = ParserConstants.DANGGNMARKET_POST_LIST + "/articles/" + articleId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {

            Document document = Jsoup.connect(url).get();

            Element subjectElement = document.getElementById("article-title");
            if(filteringWordService.stringFilter(subjectElement.text())){
                return;
            }

            Element priceElement = document.getElementById("article-price") == null ? document.getElementById("article-price-nanum") : document.getElementById("article-price");
            String stringPrice = priceElement.text().replaceAll("[^0-9]", "");

            ArticleState state = priceElement.text().equals("무료나눔") ? ArticleState.F : ArticleState.S;
            Long price = Long.parseLong(stringPrice.equals("") ? "0" : stringPrice);

            Element imageElement = document.selectFirst("[class=image-wrap]").child(0);
            Element regionElement = document.getElementById("region-name");
            Element dateElement = document.selectFirst("[datatype=xsd:date]");
            Element contentElement = document.getElementById("article-detail");

            String content = contentElement.text() + "\n" + regionElement.text();

            if(content.length() > 2000 || filteringWordService.stringFilter(content)) {
                return;
            }

            ArticleEntity articleEntity = ArticleEntity.builder()
                    .articleId(articleId)
                    .url(url)
                    .subject(subjectElement.text())
                    .price(price)
                    .state(state)
                    .image(imageElement.attr("data-lazy"))
                    .site("danggn")
                    .postingDtime(LocalDate.parse(dateElement.attr("content"), formatter).atStartOfDay(ZoneId.of("Asia/Seoul")))
                    .content(content)
                    .build();

            articleRepository.save(articleEntity);
        } catch (HttpStatusException e) {
            log.error("{} : Not found", url);
        } catch (NullPointerException e) {
            log.error("{} : Secret Article", url);
        }
    }
}
