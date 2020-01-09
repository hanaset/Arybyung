package com.how.arybyungobserver.service;

import com.how.arybyungcommon.entity.ArticleEntity;
import com.how.arybyungcommon.repository.ArticleRepository;
import com.how.arybyungobserver.client.JoonggonaraParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JoongnaraService {

    private final JoonggonaraParser joonggonaraParser;
    private final ArticleRepository articleRepository;
    private final Long defaultArticleId = 691000000L;
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

    public void parsingArticle() throws Exception {

        Long topArticleId = getTopArticleId();

        if(topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        Long recentArticleId = joonggonaraParser.getRecentArticleId();

        if (recentArticleId > topArticleId + 150) {
            recentArticleId = topArticleId + 150;
            nowArticleId = recentArticleId;
        }

        if (topArticleId.compareTo(recentArticleId) < 0) {

            for (Long i = topArticleId + 1; i <= recentArticleId; i++) {
                joonggonaraParser.getArticle(i);
            }
        }
    }
}
