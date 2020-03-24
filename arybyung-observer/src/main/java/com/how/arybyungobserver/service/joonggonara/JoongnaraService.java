package com.how.arybyungobserver.service.joonggonara;

import com.how.arybyungobserver.service.CrawlerConstant;
import com.how.muchcommon.entity.jpaentity.TopArticleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
public class JoongnaraService {

    private final JoonggonaraCrawlingService joonggonaraCrawlingService;
    private Long nowArticleId = 0L;

    public JoongnaraService(JoonggonaraCrawlingService joonggonaraCrawlingService) {
        this.joonggonaraCrawlingService = joonggonaraCrawlingService;
    }

    public void parsingArticle() {

        TopArticleEntity topArticleEntity = joonggonaraCrawlingService.getTopArticleEntity();
        Long topArticleId = topArticleEntity.getArticleId();
        Long recentArticleId = joonggonaraCrawlingService.getRecentArticleId();

        log.info("Joonggonara Top :{} , Recent : {}", topArticleId, recentArticleId);

        Long gap = recentArticleId - topArticleId;

        if (gap <= 0) {
            log.info("JoonggoNARA Not found Article");
            return;
        } else if (gap > 0 && gap <= CrawlerConstant.RANGE) {
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        } else {
            topArticleId = recentArticleId - CrawlerConstant.RANGE;
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
        topArticleEntity.setArticleId(recentArticleId);
        topArticleEntity.setUpdDtime(ZonedDateTime.now());
        joonggonaraCrawlingService.setTopArticleEntity(topArticleEntity);
    }

}