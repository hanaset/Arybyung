package com.how.arybyungobserver.service.joonggonara;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JoongnaraService {

    private final JoonggonaraCrawlingService joonggonaraCrawlingService;
    private Long nowArticleId = 0L;

    public JoongnaraService(JoonggonaraCrawlingService joonggonaraCrawlingService) {
        this.joonggonaraCrawlingService = joonggonaraCrawlingService;
    }

    public void parsingArticle() {

        Long topArticleId = joonggonaraCrawlingService.getTopArticleId();
        Long recentArticleId = joonggonaraCrawlingService.getRecentArticleId();

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

        if(topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        for (nowArticleId = topArticleId + 1; nowArticleId <= recentArticleId; nowArticleId++) {
            joonggonaraCrawlingService.getArticle(nowArticleId);
        }

        log.info("JoonggoNARA ArticleId {} ~ {}", topArticleId, recentArticleId);
    }

}
