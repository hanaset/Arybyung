package com.hanaset.arybyungobserver.service;

import com.hanaset.arybyungcommon.entity.ArticleEntity;
import com.hanaset.arybyungcommon.repository.ArticleRepository;
import com.hanaset.arybyungobserver.client.JoonggonaraParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class JoongnaraService {

    private final JoonggonaraParser joonggonaraParser;
    private final ArticleRepository articleRepository;
    private final Long defaultArticleId = 691000000L;

    public JoongnaraService(JoonggonaraParser joonggonaraParser,
                            ArticleRepository articleRepository) {
        this.joonggonaraParser = joonggonaraParser;
        this.articleRepository = articleRepository;
    }

    private Long getTopArticleId() { // DB에서 저장된 가장 최근 게시글 번호 불러오기
        List<ArticleEntity> articleEntityList = articleRepository.findByPostingDtimeAfter(ZonedDateTime.now().minusMonths(1L));
        Long articleId = articleEntityList.stream().map(ArticleEntity::getArticleId).max(Long::compareTo).orElse(defaultArticleId);

        return articleId;
    }

    public void parsingArticle() throws Exception {

        Long topArticleId = getTopArticleId();
        Long recentArticleId = joonggonaraParser.getRecentArticleId();

        if(topArticleId.compareTo(recentArticleId) < 0) {

            for(Long i = topArticleId + 1 ; i <= recentArticleId ; i++) {
                joonggonaraParser.getArticle(i);
            }
        }
    }
}
