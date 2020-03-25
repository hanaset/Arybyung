package com.how.arybyungcommon.client.danggnmarket;

import com.how.arybyungcommon.properties.UrlProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
public class DanggnMarketClient {

    private final UrlProperties urlProperties;


    public DanggnMarketClient(UrlProperties urlProperties) {
        this.urlProperties = urlProperties;
    }

    public Document getRecentArticle() throws IOException {
        return Jsoup.connect(urlProperties.getDanggnArticleUrl())
                .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                .header(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,imgwebp,*/*;q=0.8")
                .header(HttpHeaders.ACCEPT_CHARSET, "utf-8")
                .get();
    }

    public Document getArticle(Long articleId) throws IOException{

        String url = urlProperties.getDanggnArticleUrl() + "/articles/" + articleId;

        return Jsoup.connect(url)
                    .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .header(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,imgwebp,*/*;q=0.8")
                    .header(HttpHeaders.ACCEPT_CHARSET, "utf-8")
                    .get();
    }
}
