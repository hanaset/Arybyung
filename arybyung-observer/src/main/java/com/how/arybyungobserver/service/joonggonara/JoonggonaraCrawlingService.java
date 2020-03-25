package com.how.arybyungobserver.service.joonggonara;

import com.how.arybyungcommon.client.joonggonara.JoonggonaraApiClient;
import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraArticleDetailResponse;
import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraListResponse;
import com.how.arybyungcommon.properties.UrlProperties;
import com.how.arybyungcommon.service.FilteringWordService;
import com.how.arybyungobserver.service.CrawlingStatService;
import com.how.muchcommon.entity.jpaentity.ArticleEntity;
import com.how.muchcommon.entity.jpaentity.CrawlingStatEntity;
import com.how.muchcommon.entity.jpaentity.TopArticleEntity;
import com.how.muchcommon.entity.jpaentity.id.CrawlingStatId;
import com.how.muchcommon.model.type.ArticleState;
import com.how.muchcommon.model.type.MarketName;
import com.how.muchcommon.repository.jparepository.ArticleRepository;
import com.how.muchcommon.repository.jparepository.CrawlingStatRepository;
import com.how.muchcommon.repository.jparepository.TopArticleRepository;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class JoonggonaraCrawlingService {

    private Logger log = LoggerFactory.getLogger("joonggonara_log");

    private final JoonggonaraApiClient joonggonaraApiClient;
    private final TopArticleRepository topArticleRepository;
    private final CrawlingStatRepository crawlingStatRepository;
    private final ArticleRepository articleRepository;
    private final UrlProperties urlProperties;
    private final FilteringWordService filteringWordService;
    private final CrawlingStatService crawlingStatService;

    private AtomicLong successCount = new AtomicLong(0);
    private AtomicLong failCount = new AtomicLong(0);
    private AtomicLong filteringCount = new AtomicLong(0);

    public JoonggonaraCrawlingService(JoonggonaraApiClient joonggonaraApiClient,
                                      TopArticleRepository topArticleRepository,
                                      CrawlingStatRepository crawlingStatRepository,
                                      ArticleRepository articleRepository,
                                      UrlProperties urlProperties,
                                      FilteringWordService filteringWordService,
                                      CrawlingStatService crawlingStatService) {
        this.joonggonaraApiClient = joonggonaraApiClient;
        this.topArticleRepository = topArticleRepository;
        this.crawlingStatRepository = crawlingStatRepository;
        this.articleRepository = articleRepository;
        this.urlProperties = urlProperties;
        this.filteringWordService = filteringWordService;
        this.crawlingStatService = crawlingStatService;
    }

    public void saveCount() {
        crawlingStatService.save(MarketName.joonggonara, successCount.get(), failCount.get(), filteringCount.get());
        init();
    }

    @PostConstruct
    public void init() {
        CrawlingStatEntity entity = crawlingStatService.init(MarketName.joonggonara);
        successCount.set(entity.getSuccessCount());
        failCount.set(entity.getFailCount());
        filteringCount.set(entity.getFilteringCount());
    }


    public TopArticleEntity getTopArticleEntity() {
        return topArticleRepository.findBySite(MarketName.joonggonara.getName()).orElse(TopArticleEntity.builder().articleId(0L).build());
    }

    public void setTopArticleEntity(TopArticleEntity topArticleEntity) {
        topArticleRepository.save(topArticleEntity);
    }

    public Long getRecentArticleId() {
        try {
            Response<JoonggonaraListResponse> response = joonggonaraApiClient.getArticleList().execute();
            if (response.isSuccessful()) {

                Long recentArticleId = response.body().getMessage().getResult().getArticleList().stream()
                        .map(article -> article.getArticleId())
                        .max(Long::compareTo)
                        .orElse(0L);

                return recentArticleId;
            } else {
                log.error("JoonggoNARA getRecentArtilceId Failed : {}", response.errorBody().byteStream().toString());
                return 0L;
            }
        } catch (IOException e) {
            log.error("JoonggoNARA getRecentArticleId IOException : {}", e.getMessage());
            return 0L;
        }
    }

    @Async(value = "joonggonaraTaskExecutor")
    public void separationGetArticles(Long start, Long unit) {
        for (Long articleId = start; articleId < start + unit; articleId++) {
            getArticle(articleId);
        }
    }

    public void getArticle(Long articleId) {
        try {
            Response<JoonggonaraArticleDetailResponse> response = joonggonaraApiClient.getArticleDetail(articleId).execute();
            if (response.isSuccessful()) {

                if (response.body().getArticle() == null) {
                    log.info("JoonggoNara Delete Article : {}", articleId);
                    failCount.incrementAndGet();
                    return;
                }

                String content = Jsoup.parse(response.body().getArticle().getContent()).getAllElements().text();
                if (filteringWordService.stringFilter(content)) {
                    log.info("JoonggoNara filtering Check Failed : {}", articleId);
                    filteringCount.incrementAndGet();
                    return;
                }

                Instant i = Instant.ofEpochMilli(response.body().getArticle().getWriteDate());
                ZonedDateTime postDtime = ZonedDateTime.ofInstant(i, ZoneId.of("Asia/Seoul"));

                ArticleState state;
                switch (response.body().getSaleInfo().getSaleStatus()) {
                    case "SALE":
                    case "ESCROW":
                        state = ArticleState.S;
                        break;
                    case "SOLD_OUT":
                        state = ArticleState.C;
                        break;
                    default:
                        state = ArticleState.D;
                        break;
                }

                ArticleEntity articleEntity = ArticleEntity.builder()
                        .articleId(articleId)
                        .state(state)
                        .site(MarketName.joonggonara.getName())
                        .price(response.body().getSaleInfo().getPrice())
                        .url(urlProperties.getJoonggoArtlcleUrl() + articleId)
                        .subject(response.body().getArticle().getSubject())
                        .content(content)
                        .image(response.body().getSaleInfo().getImgUrl())
                        .postingDtime(postDtime)
                        .build();

                successCount.incrementAndGet();
                articleRepository.save(articleEntity);
            } else {
                log.error("JoonggoNARA getArticle Failed articleID: {} => {}", articleId, response.errorBody().byteStream().toString());
                failCount.incrementAndGet();
            }

        } catch (IOException | NullPointerException | DataIntegrityViolationException e) {
            log.error("JoonggoNARA getArticle articleId : {} => Exception : {}", articleId, e.getMessage());
            failCount.incrementAndGet();
        }
    }
}
