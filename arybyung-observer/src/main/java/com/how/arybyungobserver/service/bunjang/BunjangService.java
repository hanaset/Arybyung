package com.how.arybyungobserver.service.bunjang;

import com.how.arybyungobserver.service.CrawlerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BunjangService {

    private final BunjangCrawlingService bunjangCrawlingService;
    private Long nowArticleId = 0L;
    private Long gap = 500L;

    public BunjangService(BunjangCrawlingService bunjangCrawlingService) {
        this.bunjangCrawlingService = bunjangCrawlingService;
    }

    public void parsingArticle() {
        Long topArticleId = bunjangCrawlingService.getTopArticleId();
        Long recentArticleId = bunjangCrawlingService.getRecentArticleId();

        Long gap = recentArticleId - topArticleId;

        if (gap <= 0) {
            log.info("Bunjang Not found Article : recent = {} / top = {}", recentArticleId, topArticleId);
            return;
        } else if (gap > 0 && gap <= 100000) {
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        } else {
            topArticleId = recentArticleId - 100000;
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        }

        if(topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        for (nowArticleId = topArticleId + 1; nowArticleId <= recentArticleId; nowArticleId++) {
            bunjangCrawlingService.getArticle(nowArticleId);
        }

        log.info("Bunjang ArticleId {} ~ {}", topArticleId, recentArticleId);
    }
}
