package com.how.arybyungobserver.service.bunjang;

import com.how.arybyungobserver.service.CrawlerConstant;
import com.how.muchcommon.entity.jpaentity.TopArticleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
public class BunjangService {

    private final BunjangCrawlingService bunjangCrawlingService;
    private Long nowArticleId = 0L;

    public BunjangService(BunjangCrawlingService bunjangCrawlingService) {
        this.bunjangCrawlingService = bunjangCrawlingService;
    }

    public void parsingArticle() {
        TopArticleEntity topArticleEntity = bunjangCrawlingService.getTopArticleEntity();
        Long topArticleId = topArticleEntity.getArticleId();
        Long recentArticleId = bunjangCrawlingService.getRecentArticleId();

        log.info("Bunjang Top :{} , Recent : {}", topArticleId, recentArticleId);

        Long gap = recentArticleId - topArticleId;

        if (gap <= 0) {
            log.info("Bunjang Not found Article : recent = {} / top = {}", recentArticleId, topArticleId);
            return;
        } else if (gap > 0 && gap <= CrawlerConstant.RANGE) {
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        } else {
            topArticleId = recentArticleId - CrawlerConstant.RANGE;
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        }

        if(topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        Long threadUnit = CrawlerConstant.GAP / 10;
        for (nowArticleId = topArticleId ; nowArticleId < recentArticleId; nowArticleId += threadUnit) {
            bunjangCrawlingService.separationGetArticles(nowArticleId, threadUnit);
        }

        log.info("Bunjang ArticleId {} ~ {}", topArticleId , recentArticleId);
        topArticleEntity.setArticleId(recentArticleId);
        topArticleEntity.setUpdDtime(ZonedDateTime.now());
        bunjangCrawlingService.setTopArticleEntity(topArticleEntity);
    }
}
