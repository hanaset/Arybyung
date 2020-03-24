package com.how.arybyungobserver.service.danggnmarket;

import com.how.arybyungcommon.client.danggnmarket.DanggnMarketParser;
import com.how.arybyungobserver.service.CrawlerConstant;
import com.how.muchcommon.entity.jpaentity.ArticleEntity;
import com.how.muchcommon.entity.jpaentity.TopArticleEntity;
import com.how.muchcommon.model.type.MarketName;
import com.how.muchcommon.repository.jparepository.ArticleRepository;
import com.how.muchcommon.repository.jparepository.TopArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class DanggnMarketService {

    private final DanggnMarketParser danggnMarketParser;
    private final TopArticleRepository topArticleRepository;
    private final ArticleRepository articleRepository;
    private Long nowArticleId = 0L;


    public DanggnMarketService(DanggnMarketParser danggnMarketParser,
                               TopArticleRepository topArticleRepository,
                               ArticleRepository articleRepository) {
        this.danggnMarketParser = danggnMarketParser;
        this.topArticleRepository = topArticleRepository;
        this.articleRepository = articleRepository;
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
        Long recentArticleId = danggnMarketParser.getRecentArticleId();
        Long gap = recentArticleId - topArticleId;

        log.info("Danggn Top :{} , Recent : {}", topArticleId, recentArticleId);

        if (gap <= 0) {
            log.info("DanggnMarket Not found Article");
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

        Long threadUnit = CrawlerConstant.GAP / 10;
        for (nowArticleId = topArticleId; nowArticleId < recentArticleId; nowArticleId += threadUnit) {
            danggnMarketParser.separationGetArticles(nowArticleId, threadUnit);
        }
        log.info("DanggnMarket ArticleId {} ~ {}", topArticleId, recentArticleId);
        topArticleEntity.setArticleId(recentArticleId);
        setTopArticleEntity(topArticleEntity);

    }
}
