package com.how.arybyungprovider.service.validation;

import com.how.arybyungcommon.client.danggnmarket.DanggnMarketClient;
import com.how.arybyungcommon.properties.UrlProperties;
import com.how.muchcommon.model.type.ArticleState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class DanggnValidation implements ValidationMarket {

    private final DanggnMarketClient danggnMarketClient;

    @Override
    public ArticleState validationMarket(long articleId) {

        try {
            Document document = danggnMarketClient.getArticle(articleId);

            Element subjectElement = document.getElementById("article-title");

            return subjectElement == null ? ArticleState.D : ArticleState.S;

        } catch (IOException e) {
            log.error("validation Danggn {} = > IOException : {}", articleId, e.getMessage());
        }
        return ArticleState.D;
    }
}
