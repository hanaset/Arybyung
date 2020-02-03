package com.how.arybyungobserver.service;

import com.how.arybyungobserver.client.DanggnMarketParser;
import com.how.muchcommon.entity.japentity.ArticleEntity;
import com.how.muchcommon.repository.jparepository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class DanggnMarketService {

    private final DanggnMarketParser danggnMarketParser;
    private final ArticleRepository articleRepository;
    private Long nowArticleId = 0L;

    public DanggnMarketService(DanggnMarketParser danggnMarketParser,
                               ArticleRepository articleRepository) {
        this.danggnMarketParser = danggnMarketParser;
        this.articleRepository = articleRepository;
    }

    private Long getTopArticleId() {
        ArticleEntity articleEntity = articleRepository.findTopBySiteOrderByArticleIdDesc("danggn").orElse(ArticleEntity.builder()
                .articleId(0L).build());
        return articleEntity.getArticleId();
    }

    public void parsingArticle() throws IOException {
        Long topArticleId = getTopArticleId();
        Long recentArticleId = danggnMarketParser.getRecentArticleId();
        Long gap = recentArticleId - topArticleId;

        if (gap <= 0) {
            log.info("DanggnMarket Not found Article");
            return;
        } else if (gap > 0 && gap <= 100000) {
            recentArticleId = topArticleId + 150;
        } else {
            topArticleId = recentArticleId - 100000;
            recentArticleId = topArticleId + 150;
        }


        for (nowArticleId = topArticleId + 1; nowArticleId <= recentArticleId; nowArticleId++) {
            danggnMarketParser.getArticle(nowArticleId);
        }
        log.info("DanggnMarket ArticleId {} ~ {}", topArticleId, recentArticleId);

    }
}
