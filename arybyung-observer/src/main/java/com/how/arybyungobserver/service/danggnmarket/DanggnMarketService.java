package com.how.arybyungobserver.service.danggnmarket;

import com.how.arybyungcommon.client.danggnmarket.DanggnMarketClient;
import com.how.arybyungobserver.service.CrawlerConstant;
import com.how.muchcommon.entity.jpaentity.TopArticleEntity;
import com.how.muchcommon.model.type.MarketName;
import com.how.muchcommon.repository.jparepository.TopArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class DanggnMarketService {

    private final DanggnCrawlingService danggnCrawlingService;
    private final TopArticleRepository topArticleRepository;
    private Long nowArticleId = 0L;


    public DanggnMarketService(DanggnCrawlingService danggnCrawlingService,
                               TopArticleRepository topArticleRepository) {
        this.danggnCrawlingService = danggnCrawlingService;
        this.topArticleRepository = topArticleRepository;
    }

    public void saveCount() {
        danggnCrawlingService.saveCount();
    }
    public TopArticleEntity getTopArticleEntity() {
        return topArticleRepository.findBySite(MarketName.danggn.getName()).orElse(TopArticleEntity.builder().articleId(0L).build());
    }

    public void setTopArticleEntity(TopArticleEntity topArticleEntity) {
        topArticleRepository.save(topArticleEntity);
    }

    public void parsingArticle() throws IOException {
        TopArticleEntity topArticleEntity = getTopArticleEntity();
        Long topArticleId = topArticleEntity.getArticleId();
        Long recentArticleId = danggnCrawlingService.getRecentArticleId();
        Long gap = recentArticleId - topArticleId;

        log.info("Danggn Top :{} , Recent : {}", topArticleId, recentArticleId);

        if (gap <= 0) {
            log.info("DanggnMarket Not found Article");
            return;
        } else if (gap > 0 && gap <= CrawlerConstant.RANGE) {

            if(gap > CrawlerConstant.GAP) {
                recentArticleId = topArticleId + CrawlerConstant.GAP;
            }
        } else {
            topArticleId = recentArticleId - CrawlerConstant.RANGE;
            recentArticleId = topArticleId + CrawlerConstant.GAP;
        }

        if(topArticleId < nowArticleId) {
            topArticleId = nowArticleId;
        }

        Long threadUnit = CrawlerConstant.GAP / 10;
        for (nowArticleId = topArticleId; nowArticleId < recentArticleId; nowArticleId += threadUnit) {
            danggnCrawlingService.separationGetArticles(nowArticleId, threadUnit);
        }
        log.info("DanggnMarket ArticleId {} ~ {}", topArticleId, recentArticleId);
        topArticleEntity.setArticleId(recentArticleId);
        topArticleEntity.setUpdDtime(ZonedDateTime.now());
        setTopArticleEntity(topArticleEntity);

    }
}
