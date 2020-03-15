package com.how.arybyungobserver.service.bunjang;

import com.how.arybyungobserver.client.bunjang.BunjangApiClient;
import com.how.arybyungobserver.client.bunjang.model.BunjangHomeResponse;
import com.how.arybyungobserver.client.bunjang.model.BunjangItemResponse;
import com.how.arybyungobserver.properties.UrlProperties;
import com.how.arybyungobserver.service.FilteringWordService;
import com.how.muchcommon.entity.jpaentity.ArticleEntity;
import com.how.muchcommon.model.type.ArticleState;
import com.how.muchcommon.repository.jparepository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class BunjangCrawlingService {

    private final BunjangApiClient bunjangApiClient;
    private final ArticleRepository articleRepository;
    private final UrlProperties urlProperties;
    private final FilteringWordService filteringWordService;

    public BunjangCrawlingService(BunjangApiClient bunjangApiClient,
                                  ArticleRepository articleRepository,
                                  UrlProperties urlProperties,
                                  FilteringWordService filteringWordService) {
        this.bunjangApiClient = bunjangApiClient;
        this.articleRepository = articleRepository;
        this.urlProperties = urlProperties;
        this.filteringWordService = filteringWordService;
    }

    public Long getTopArticleId() {
        ArticleEntity articleEntity = articleRepository.findTopBySiteOrderByArticleIdDesc("bunjang").orElse(ArticleEntity.builder()
                .articleId(0L).build());
        return articleEntity.getArticleId();
    }

    public Long getRecentArticleId() {
        try {
            Response<BunjangHomeResponse> response = bunjangApiClient.getArticleList().execute();
            if(response.isSuccessful()) {

                Long recentArticleId = response.body().getList().stream()
                        .map(bunjangHomeItem -> Long.parseLong(bunjangHomeItem.getPid()))
                        .max(Long::compareTo)
                        .orElse(0L);

                return recentArticleId;
            } else {
                log.error("Bunjang getRecentArtilceId Failed : {}", response.errorBody().byteStream().toString());
                return 0L;
            }
        }catch (IOException e) {
            log.error("Bunjang getRecentArticleId IOException : {}", e.getMessage());
            return 0L;
        }
    }

    @Async(value = "bunjangTaskExecutor")
    @Transactional
    public void getArticle(Long articleId) {
        try {
            Response<BunjangItemResponse> response = bunjangApiClient.getArticle(articleId).execute();
            if(response.isSuccessful()) {

                if(response.body().getItemInfo() == null) {
                    return;
                }

                if(filteringWordService.stringFilter(response.body().getItemInfo().getDescription())) {
                    return;
                }

                Instant i = Instant.ofEpochSecond(response.body().getItemInfo().getUpdateTime());
                ZonedDateTime postDtime = ZonedDateTime.ofInstant(i, ZoneId.of("Asia/Seoul"));

                ArticleEntity articleEntity = ArticleEntity.builder()
                        .articleId(articleId)
                        .state(ArticleState.S)
                        .site("bunjang")
                        .price(Long.parseLong(response.body().getItemInfo().getPrice()))
                        .url(urlProperties.getBunjangArticleUrl() + articleId)
                        .subject(response.body().getItemInfo().getName())
                        .content(response.body().getItemInfo().getDescription() + "\n" + response.body().getItemInfo().getLocation())
                        .image(response.body().getItemInfo().getProductImage())
                        .postingDtime(postDtime)
                        .build();

                articleRepository.save(articleEntity);
            } else {
                log.error("Bunjang getArticle Failed :{}",  response.errorBody().byteStream().toString());
            }

        } catch (IOException | NullPointerException e) {
            log.error("Bunjang getArticle IOException : {}", e.getMessage());
        }
    }
}
