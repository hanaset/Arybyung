package com.how.arybyungobserver.service.bunjang;

import com.how.arybyungcommon.client.bunjang.BunjangApiClient;
import com.how.arybyungcommon.client.bunjang.model.BunjangHomeResponse;
import com.how.arybyungcommon.client.bunjang.model.BunjangItemResponse;
import com.how.arybyungcommon.properties.UrlProperties;
import com.how.arybyungcommon.service.FilteringWordService;
import com.how.arybyungobserver.service.CrawlingStatService;
import com.how.muchcommon.entity.jpaentity.ArticleEntity;
import com.how.muchcommon.entity.jpaentity.CrawlingStatEntity;
import com.how.muchcommon.entity.jpaentity.TopArticleEntity;
import com.how.muchcommon.model.type.ArticleState;
import com.how.muchcommon.model.type.MarketName;
import com.how.muchcommon.repository.jparepository.ArticleRepository;
import com.how.muchcommon.repository.jparepository.TopArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BunjangCrawlingService {

    private Logger log = LoggerFactory.getLogger("bunjang_log");

    private final BunjangApiClient bunjangApiClient;
    private final TopArticleRepository topArticleRepository;
    private final ArticleRepository articleRepository;
    private final UrlProperties urlProperties;
    private final FilteringWordService filteringWordService;
    private final CrawlingStatService crawlingStatService;

    private AtomicLong successCount = new AtomicLong(0);
    private AtomicLong failCount = new AtomicLong(0);
    private AtomicLong filteringCount = new AtomicLong(0);

    public BunjangCrawlingService(BunjangApiClient bunjangApiClient,
                                  TopArticleRepository topArticleRepository,
                                  ArticleRepository articleRepository,
                                  UrlProperties urlProperties,
                                  FilteringWordService filteringWordService,
                                  CrawlingStatService crawlingStatService) {
        this.bunjangApiClient = bunjangApiClient;
        this.topArticleRepository = topArticleRepository;
        this.articleRepository = articleRepository;
        this.urlProperties = urlProperties;
        this.filteringWordService = filteringWordService;
        this.crawlingStatService = crawlingStatService;
    }

    public void saveCount() {
        crawlingStatService.save(MarketName.bunjang, successCount.get(), failCount.get(), filteringCount.get());
        init();
    }

    @PostConstruct
    public void init() {
        CrawlingStatEntity entity = crawlingStatService.init(MarketName.bunjang);
        successCount.set(entity.getSuccessCount());
        failCount.set(entity.getFailCount());
        filteringCount.set(entity.getFilteringCount());
    }

    public TopArticleEntity getTopArticleEntity() {
        return topArticleRepository.findBySite(MarketName.bunjang.getName()).orElse(TopArticleEntity.builder().articleId(0L).build());
    }

    public void setTopArticleEntity(TopArticleEntity topArticleEntity) {
        topArticleRepository.save(topArticleEntity);
    }

    public Long getRecentArticleId() {
        try {
            Response<BunjangHomeResponse> response = bunjangApiClient.getArticleList().execute();
            if (response.isSuccessful()) {

                Long recentArticleId = response.body().getList().stream()
                        .map(bunjangHomeItem -> Long.parseLong(bunjangHomeItem.getPid()))
                        .max(Long::compareTo)
                        .orElse(0L);

                return recentArticleId;
            } else {
                log.error("Bunjang getRecentArtilceId Failed : {}", response.errorBody().byteStream().toString());
                return 0L;
            }
        } catch (IOException e) {
            log.error("Bunjang getRecentArticleId IOException : {}", e.getMessage());
            return 0L;
        }
    }

    @Async("bunjangTaskExecutor")
    public void separationGetArticles(Long start, Long unit) {
        for (Long articleId = start; articleId < start + unit; articleId++) {
            getArticle(articleId);
        }
    }

    public void getArticle(Long articleId) {
        try {
            Response<BunjangItemResponse> response = bunjangApiClient.getArticle(articleId).execute();
            if (response.isSuccessful()) {

                if (response.body().getItemInfo() == null) {
                    log.info("bunjang Delete Article : {}", articleId);
                    failCount.incrementAndGet();
                    return;
                }

                if (filteringWordService.stringFilter(response.body().getItemInfo().getDescription())) {
                    log.info("bunjang filtering Check Failed : {}", articleId);
                    filteringCount.incrementAndGet();
                    return;
                }

                Instant i = Instant.ofEpochSecond(response.body().getItemInfo().getUpdateTime());
                ZonedDateTime postDtime = ZonedDateTime.ofInstant(i, ZoneId.of("Asia/Seoul"));

                ArticleEntity articleEntity = ArticleEntity.builder()
                        .articleId(articleId)
                        .state(ArticleState.S)
                        .site(MarketName.bunjang.getName())
                        .price(Long.parseLong(response.body().getItemInfo().getPrice()))
                        .url(urlProperties.getBunjangArticleUrl() + articleId)
                        .subject(response.body().getItemInfo().getName())
                        .content(response.body().getItemInfo().getDescription() + "\n" + response.body().getItemInfo().getLocation())
                        .image(response.body().getItemInfo().getProductImage())
                        .postingDtime(postDtime)
                        .build();

                successCount.incrementAndGet();
                articleRepository.save(articleEntity);
            } else {
                log.error("Bunjang getArticle Failed :{}", response.errorBody().byteStream().toString());
                failCount.incrementAndGet();
            }

        } catch (IOException | NullPointerException e) {
            log.error("Bunjang getArticle IOException : {}", e.getMessage());
            failCount.incrementAndGet();
        }
    }
}
