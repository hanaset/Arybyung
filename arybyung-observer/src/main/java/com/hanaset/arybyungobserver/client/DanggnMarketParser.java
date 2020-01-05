package com.hanaset.arybyungobserver.client;

import com.hanaset.arybyungcommon.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class DanggnMarketParser {

    private final ArticleRepository articleRepository;

    public DanggnMarketParser(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Long getRecentArticleId() throws IOException {
        Document document = Jsoup.connect(ParserConstants.DANGGNMARKET_POST_LIST)
                .get();

        List<Element> elements = document.select("[data-event-label]");
        Long maxArticleId = elements.stream().map(element -> Long.parseLong(element.attr("data-event-label"))).max(Long::compareTo).orElse(0L);

        return maxArticleId;
    }

    public void getArticle(Long articleId) throws IOException{

        String url = ParserConstants.DANGGNMARKET_POST_LIST + "/articles/" + articleId;

        Document document = Jsoup.connect(url).get();

        Element subjectElement = document.getElementById("article-title");
        System.out.println(subjectElement.text());

        Element priceElement = document.getElementById("article-price");
        System.out.println(priceElement.text());

        Element imageElement = document.selectFirst("[class=portrait]");
        System.out.println(imageElement.attr("data-lazy"));

        Element regionElement = document.getElementById("region-name");
        System.out.println(regionElement.text());

        Element dateElement = document.selectFirst("[datatype=xsd:date]");
        System.out.println(dateElement.attr("content"));

        Element contentElement = document.getElementById("article-detail");
        System.out.println(contentElement.text());
    }
}
