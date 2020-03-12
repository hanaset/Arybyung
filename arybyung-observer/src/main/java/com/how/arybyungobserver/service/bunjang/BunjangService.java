package com.how.arybyungobserver.service.bunjang;

import com.how.arybyungobserver.client.DriverConstants;
import com.how.arybyungobserver.client.bunjang.BunjangApiClient;
import com.how.arybyungobserver.client.bunjang.model.BunjangHomeItem;
import com.how.arybyungobserver.client.bunjang.model.BunjangHomeResponse;
import com.how.arybyungobserver.client.bunjang.model.BunjangItemResponse;
import com.how.arybyungobserver.properties.UrlProperties;
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
public class BunjangService {

    private final BunjangApiClient bunjangApiClient;
    private final ArticleRepository articleRepository;
    private final UrlProperties urlProperties;
    private Long nowArticleId = 0L;

    public BunjangService(BunjangApiClient bunjangApiClient,
                          ArticleRepository articleRepository,
                          UrlProperties urlProperties) {
        this.bunjangApiClient = bunjangApiClient;
        this.articleRepository = articleRepository;
        this.urlProperties = urlProperties;
    }

    private Long getTopArticleId() {
        ArticleEntity articleEntity = articleRepository.findTopBySiteOrderByArticleIdDesc("bunjang").orElse(ArticleEntity.builder()
                .articleId(0L).build());
        return articleEntity.getArticleId();
    }

    private Long getRecentArticleId() {
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

        } catch (IOException e) {
            log.error("Bunjang getArticle IOException : {}", e.getMessage());
        }
    }

    public void parsingArticle() {
        Long topArticleId = getTopArticleId();
        Long recentArticleId = getRecentArticleId();

        Long gap = recentArticleId - topArticleId;

        if (gap <= 0) {
            log.info("Bunjang Not found Article : recent = {} / top = {}", recentArticleId, topArticleId);
            return;
        } else if (gap > 0 && gap <= 100000) {
            recentArticleId = topArticleId + 150;
        } else {
            topArticleId = recentArticleId - 100000;
            recentArticleId = topArticleId + 150;
        }

        for (nowArticleId = topArticleId + 1; nowArticleId <= recentArticleId; nowArticleId++) {
            getArticle(nowArticleId);
        }

        log.info("Bunjang ArticleId {} ~ {}", topArticleId, recentArticleId);
    }
}
