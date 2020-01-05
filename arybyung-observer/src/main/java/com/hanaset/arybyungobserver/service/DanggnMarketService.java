package com.hanaset.arybyungobserver.service;

import com.hanaset.arybyungcommon.entity.ArticleEntity;
import com.hanaset.arybyungcommon.repository.ArticleRepository;
import com.hanaset.arybyungobserver.client.DanggnMarketParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DanggnMarketService {

    private final DanggnMarketParser danggnMarketParser;
    private final ArticleRepository articleRepository;
    private final Long defaultArticleId = 54593000L;

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
}
