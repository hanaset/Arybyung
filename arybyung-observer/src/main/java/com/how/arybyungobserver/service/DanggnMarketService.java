package com.how.arybyungobserver.service;

import com.how.muchcommon.entity.ArticleEntity;
import com.how.muchcommon.repository.ArticleRepository;
import com.how.arybyungobserver.client.DanggnMarketParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class DanggnMarketService {

    private final DanggnMarketParser danggnMarketParser;
    private final ArticleRepository articleRepository;
    private final Long defaultArticleId = 55000000L;
    private Long nowArticleId = 0L;

    public DanggnMarketService(DanggnMarketParser danggnMarketParser,
                               ArticleRepository articleRepository) {
        this.danggnMarketParser = danggnMarketParser;
        this.articleRepository = articleRepository;
    }

    private Long getTopArticleId() {
        ArticleEntity articleEntity = articleRepository.findTopBySiteOrderByArticleIdDesc("danggn").orElse(ArticleEntity.builder()
                .articleId(defaultArticleId).build());
        return articleEntity.getArticleId();
    }

    public void parsingArticle() throws IOException {
        Long topArticleId = getTopArticleId();
        Long recentArticleId = danggnMarketParser.getRecentArticleId();

        if(topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        if(recentArticleId > topArticleId + 500) {
            recentArticleId = topArticleId + 500;
        }

        if(topArticleId.compareTo(recentArticleId) < 0) {

            for( Long i = topArticleId + 1 ; i <= recentArticleId ; i ++) {
                danggnMarketParser.getArticle(i);
            }
        }
    }
}
