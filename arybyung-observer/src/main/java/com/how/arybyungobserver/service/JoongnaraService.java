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

        if (topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        Long recentArticleId = joonggonaraParser.getRecentArticleId();

        if (recentArticleId > topArticleId + 150) {
            recentArticleId = topArticleId + 150;
        }

        if (topArticleId.compareTo(recentArticleId) < 0) {

            for (nowArticleId = topArticleId + 1; nowArticleId <= recentArticleId; nowArticleId++) {
                joonggonaraParser.getArticle(nowArticleId);
            }

            log.info("JoonggoNARA ArticleId {} ~ {}", topArticleId, recentArticleId);
        } else {
            log.info("JoonggoNARA Not found Article");
        }
    }
}
