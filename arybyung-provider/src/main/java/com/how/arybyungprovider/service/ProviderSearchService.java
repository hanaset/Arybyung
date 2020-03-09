package com.how.arybyungprovider.service;

import com.google.common.collect.Maps;
import com.how.arybyungprovider.constant.ProviderApiErrorCode;
import com.how.arybyungprovider.exception.ProviderResponseException;
import com.how.arybyungprovider.model.ArticleData;
import com.how.arybyungprovider.model.KeywordResultData;
import com.how.arybyungprovider.model.kakao.response.KakaoResponse;
import com.how.arybyungprovider.model.kakao.response.KakaoResponseTemplate;
import com.how.arybyungprovider.model.kakao.response.template.listcard.ListCard;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ProviderSearchService {

    private final ProviderEsService providerEsService;
    private final RedisTemplate redisTemplate;

    public ProviderSearchService(ProviderEsService providerEsService,
                                 RedisTemplate redisTemplate) {
        this.providerEsService = providerEsService;
        this.redisTemplate = redisTemplate;
    }

    public Object searchKeyword(String keyword) {

        if(!redisTemplate.hasKey(keyword)) {
            return registerResult(keyword, providerEsService.searchKeyword(keyword));
        } else {
            return redisTemplate.opsForValue().get(keyword);
        }
    }

    private KakaoResponse registerResult(String keyword, List<ArticleData> articleDatas) {

        // 키워드에 대한 결과가 없을 경우 예외처리
        if(articleDatas.isEmpty()) {
            throw new ProviderResponseException(ProviderApiErrorCode.DATA_NOT_FOUND, "키워드에 적합한 결과가 없습니다.");
        }

        // 하루 전 데이터부터 지금까지 (24시간 데이터)
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        // 24시간 기준 최고가, 최저가
        Long todayHightestPrice = articleDatas.stream().filter(articleData -> articleData.getPostingDtime().isAfter(today)).map(ArticleData::getPrice).max(Long::compareTo).orElse(0L);
        Long todayLowestPrice = articleDatas.stream().filter(articleData -> articleData.getPostingDtime().isAfter(today)).map(ArticleData::getPrice).min(Long::compareTo).orElse(0L);

        // 1주일 기준 최고가, 최저가 (List에는 1주일동안의 데이터만 존재함)
        Long thisWeekHighestPrice = articleDatas.stream().map(ArticleData::getPrice).max(Long::compareTo).orElse(0L);
        Long thisWeekLowestPrice = articleDatas.stream().map(ArticleData::getPrice).min(Long::compareTo).orElse(0L);

        // TODO response 만들기
//        ListCard listCard = ListCard.builder()
//                .header()
//                .items()
//                .buttons()
//                .build();
//
//        KakaoResponseTemplate<ListCard> kakaoResponseTemplate = KakaoResponseTemplate.builder()
//                .outputs()
//                .build();
//        KakaoResponse kakaoResponse = KakaoResponse.builder()
//                .version("v1.0")
//                .template()
//                .build();
        KeywordResultData keywordResultData = KeywordResultData.builder()
                .articleList(articleDatas)
                .todayHighestPrice(todayHightestPrice)
                .todayLowestPrice(todayLowestPrice)
                .thisWeekHighestPrice(thisWeekHighestPrice)
                .thisWeekLowestPrice(thisWeekLowestPrice)
                .build();

        // Redis에 키워드에 결과 저장 (만료시간 1시간)
        redisTemplate.opsForValue().set(keyword, keywordResultData, 60, TimeUnit.MINUTES);

        return keywordResultData;
    }
}
