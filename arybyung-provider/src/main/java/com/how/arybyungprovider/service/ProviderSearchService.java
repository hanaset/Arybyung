package com.how.arybyungprovider.service;

import com.how.arybyungprovider.model.ArticleData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ProviderSearchService {

    private final ProviderEsService providerEsService;
    private final RedisTemplate redisTemplate;

    public ProviderSearchService(ProviderEsService providerEsService,
                                 RedisTemplate redisTemplate) {
        this.providerEsService = providerEsService;
        this.redisTemplate = redisTemplate;
    }

    public void searchKeyword(String keyword) {


        if(!redisTemplate.hasKey(keyword)) {
            registerResult(providerEsService.searchKeyword(keyword));
        }
    }

    private void registerResult(List<ArticleData> articleDatas) {

        // 하루 전 데이터부터 지금까지 (24시간 데이터)
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        articleDatas.stream().filter(articleData -> articleData.getPostingDtime().)


    }
}
