package com.how.arybyungprovider.service;

import com.google.common.collect.Lists;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProviderArticleService {

    private final ArticleEsRepository articleEsRepository;
    private final RedisTemplate redisTemplate;
    private final ProviderEsService providerEsService;
    private final ValidationFactory validationFactory;

    public ProviderArticleService(ArticleEsRepository articleEsRepository,
                                  RedisTemplate redisTemplate,
                                  ProviderEsService providerEsService,
                                  ValidationFactory validationFactory) {
        this.articleEsRepository = articleEsRepository;
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

}
