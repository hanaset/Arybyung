package com.how.arybyungprovider.service;

import com.how.arybyungprovider.model.KeywordResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProviderSearchService {

    private final ProviderArticleService providerArticleService;
    private final ProviderEsService providerEsService;
    private final RedisTemplate redisTemplate;
    private final PopularRankingService popularRankingService;

    public ProviderSearchService(ProviderArticleService providerArticleService,
                                 ProviderEsService providerEsService,
                                 RedisTemplate redisTemplate, PopularRankingService popularRankingService) {
        this.providerArticleService = providerArticleService;
        this.providerEsService = providerEsService;
        this.redisTemplate = redisTemplate;
        this.popularRankingService = popularRankingService;
    }

    public Object basicSearchKeyword(String keyword) {
        if (!redisTemplate.hasKey(keyword)) {
            KeywordResultData result = providerEsService.searchResult(keyword);
            redisTemplate.opsForValue().set(keyword, result, 1, TimeUnit.MINUTES);
            providerArticleService.validationArticle(keyword, result);
            return result;
        } else {
            popularRankingService.countingPopularRank(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.HOURS), keyword);
            return redisTemplate.opsForValue().get(keyword);
        }
    }
}
