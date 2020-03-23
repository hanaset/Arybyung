package com.how.arybyungobserver.service.joonggonara;

import com.how.arybyungobserver.service.CrawlerConstant;
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

        log.info("Joonggonara Top :{} , Recent : {}", topArticleId, recentArticleId);

        Long gap = recentArticleId - topArticleId;

        if (gap <= 0) {
            log.info("JoonggoNARA Not found Article");
            return;
        } else if (gap > 0 && gap <= 100000) {
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        } else {
            topArticleId = recentArticleId - 100000;
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        }

        if (topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        Long threadUnit = CrawlerConstant.GAP / 10;
        for (nowArticleId = topArticleId ; nowArticleId < recentArticleId; nowArticleId += threadUnit) {
            joonggonaraCrawlingService.separationGetArticles(nowArticleId, threadUnit);
        }

        log.info("JoonggoNARA ArticleId {} ~ {}", topArticleId, recentArticleId);
    }

}