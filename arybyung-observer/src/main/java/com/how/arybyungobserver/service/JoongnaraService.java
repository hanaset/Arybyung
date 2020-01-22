package com.how.arybyungobserver.service;

import com.how.arybyungobserver.client.JoonggonaraParser;
import com.how.muchcommon.entity.ArticleEntity;
import com.how.muchcommon.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JoongnaraService {

    private final JoonggonaraParser joonggonaraParser;
    private final ArticleRepository articleRepository;
    private final Long defaultArticleId = 697676742L;
    private Long nowArticleId = 0L;

    public JoongnaraService(JoonggonaraParser joonggonaraParser,
                            ArticleRepository articleRepository) {
        this.joonggonaraParser = joonggonaraParser;
        this.articleRepository = articleRepository;
    }

    private Long getTopArticleId() { // DB에서 저장된 가장 최근 게시글 번호 불러오기
        ArticleEntity articleEntity = articleRepository.findTopBySiteOrderByArticleIdDesc("joonggonara").orElse(ArticleEntity.builder().articleId(defaultArticleId).build());
        return articleEntity.getArticleId();
    }

    public void parsingArticle() {

        joonggonaraParser.setCount(0);

        Long topArticleId = getTopArticleId();
        Long recentArticleId = joonggonaraParser.getRecentArticleId();

        Long gap = recentArticleId - topArticleId;

        if (gap <= 0) {
            log.info("JoonggoNARA Not found Article");
            return;
        } else if (gap > 0 && gap <= 100000) {
            recentArticleId = topArticleId + 150;
        } else {
            topArticleId = recentArticleId - 100000;
            recentArticleId = topArticleId + 150;
        }

        for (nowArticleId = topArticleId + 1; nowArticleId <= recentArticleId; nowArticleId++) {
            joonggonaraParser.getArticle(nowArticleId);
        }

        log.info("JoonggoNARA ArticleId {} ~ {}", topArticleId, recentArticleId);

    }
}
