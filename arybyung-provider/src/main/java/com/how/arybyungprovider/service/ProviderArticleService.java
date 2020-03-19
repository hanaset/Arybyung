package com.how.arybyungprovider.service;

import com.google.common.collect.Lists;
import com.how.arybyungcommon.client.bunjang.BunjangApiClient;
import com.how.arybyungcommon.client.bunjang.model.BunjangItemResponse;
import com.how.arybyungcommon.client.danggnmarket.DanggnMarketParser;
import com.how.arybyungcommon.client.joonggonara.JoonggonaraApiClient;
import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraArticleDetailResponse;
import com.how.arybyungprovider.model.ArticleData;
import com.how.arybyungprovider.model.KeywordResultData;
import com.how.arybyungprovider.service.validation.ValidationFactory;
import com.how.arybyungprovider.service.validation.ValidationMarket;
import com.how.muchcommon.entity.elasticentity.ArticleEsEntity;
import com.how.muchcommon.model.type.ArticleState;
import com.how.muchcommon.repository.elasticrepository.ArticleEsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProviderArticleService {

    private final ArticleEsRepository articleEsRepository;
    private final BunjangApiClient bunjangApiClient;
    private final DanggnMarketParser danggnMarketParser;
    private final JoonggonaraApiClient joonggonaraApiClient;
    private final RedisTemplate redisTemplate;
    private final ProviderEsService providerEsService;
    private final ValidationFactory validationFactory;

    public ProviderArticleService(ArticleEsRepository articleEsRepository,
                                  BunjangApiClient bunjangApiClient,
                                  DanggnMarketParser danggnMarketParser,
                                  JoonggonaraApiClient joonggonaraApiClient,
                                  RedisTemplate redisTemplate,
                                  ProviderEsService providerEsService,
                                  ValidationFactory validationFactory) {
        this.articleEsRepository = articleEsRepository;
        this.bunjangApiClient = bunjangApiClient;
        this.danggnMarketParser = danggnMarketParser;
        this.joonggonaraApiClient = joonggonaraApiClient;
        this.redisTemplate = redisTemplate;
        this.providerEsService = providerEsService;
        this.validationFactory = validationFactory;
    }

    @Async
    public void validationArticle(String keyword, KeywordResultData resultData) {

        if(resultData.getValidating()) {
            return;
        }

        List<ArticleData> articleDataList = resultData.getArticleList();
        List<ArticleEsEntity> articleList = Lists.newArrayList();
        articleDataList.forEach(articleData -> {

            ValidationMarket validationMarket = validationFactory.getInstanseBySite(articleData.getSite());
            ArticleState state = validationMarket.validationMarket(articleData.getArticleId());
//            switch (articleData.getSite()) {
//                case "joonggonara":
//                    state = validationJoonggonara(articleData.getArticleId());
//                    break;
//                case "danggn":
//                    state = validationDanggn(articleData.getArticleId());
//                    break;
//                case "bunjang":
//                    state = validationBunjang(articleData.getArticleId());
//                    break;
//            }

            if(state.equals(ArticleState.D) || state.equals(ArticleState.C)) {
                articleList.add(
                        ArticleEsEntity.builder()
                        .id(articleData.getId())
                        .build());
            }
        });

        articleEsRepository.deleteAll(articleList);
        KeywordResultData validResult = providerEsService.searchResult(keyword);
        validResult.setValidating(true);
        redisTemplate.opsForValue().set(keyword, validResult, 10, TimeUnit.MINUTES);
        log.info("DeadLink Check completed");
    }

    private ArticleState validationBunjang(Long articleId) {
        try {
            Response<BunjangItemResponse> response = bunjangApiClient.getArticle(articleId).execute();

            if(response.body().getSellerInfo() == null) {
                return ArticleState.D;
            }
        } catch (IOException e) {
            log.error("validation Bunjang {} = > IOException : {}", articleId, e.getMessage());
        }

        return ArticleState.S;
    }

    private ArticleState validationJoonggonara(Long articleId) {
        try {
            Response<JoonggonaraArticleDetailResponse> response = joonggonaraApiClient.getArticleDetail(articleId).execute();

            if(response.code() == 200) {
                String status = response.body().getSaleInfo().getSaleStatus();

                if(!status.equals("SALE") && !status.equals("ESCROW"))
                    return ArticleState.C;

            } else if(response.code() == 404) { // 삭제된 게시글일 경우
                return ArticleState.D;
            }
        } catch (IOException e) {
            log.error("validation JoonggoNARA {} = > IOException : {}", articleId, e.getMessage());
        }

        return ArticleState.S;
    }

    private ArticleState validationDanggn(Long articleId) {
        return danggnMarketParser.validationArticle(articleId) ? ArticleState.S : ArticleState.D;
    }
}
