package com.how.arybyungprovider.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.how.arybyungprovider.constant.ProviderApiErrorCode;
import com.how.arybyungprovider.exception.ProviderResponseException;
import com.how.arybyungprovider.model.ArticleData;
import com.how.arybyungprovider.model.KeywordResultData;
import com.how.muchcommon.model.kakaobuilder.response.KakaoResponse;
import com.how.muchcommon.model.kakaobuilder.response.KakaoResponseTemplate;
import com.how.muchcommon.model.kakaobuilder.response.template.listcard.ListCard;
import com.how.muchcommon.model.kakaobuilder.response.template.listcard.ListCardHeader;
import com.how.muchcommon.model.kakaobuilder.response.template.listcard.ListCardItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProviderSearchService {

    private final ProviderEsService providerEsService;
    private final RedisTemplate redisTemplate;
    private final PopularRankingService popularRankingService;

    public ProviderSearchService(ProviderEsService providerEsService,
                                 RedisTemplate redisTemplate, PopularRankingService popularRankingService) {
        this.providerEsService = providerEsService;
        this.redisTemplate = redisTemplate;
        this.popularRankingService = popularRankingService;
    }

    public Object basicSearchKeyword(String keyword) {
        if (!redisTemplate.hasKey(keyword)) {
            return searchResult(keyword, providerEsService.searchKeyword(keyword));
        } else {
            popularRankingService.countingPopularRank(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.HOURS), keyword);
            return redisTemplate.opsForValue().get(keyword);
        }
    }

    @Deprecated
    public KakaoResponse<?> kakaoSearchKeyword(String keyword) {
        Gson gson = new Gson();
        if (!redisTemplate.hasKey("KAKAO_" + keyword)) {
            return openBuilderResult(keyword, providerEsService.searchKeyword(keyword));
        } else {
            // return mapper.readValue((String) Objects.requireNonNull(redisTemplate.opsForValue().get(keyword)), KakaoResponse.class);
            return (KakaoResponse<?>) gson.fromJson((String) redisTemplate.opsForValue().get("KAKAO_" + keyword), KakaoResponse.class);
        }
    }

    @Deprecated
    private KakaoResponse<?> openBuilderResult(String keyword, List<ArticleData> articleDatas) {

        // 키워드에 대한 결과가 없을 경우 예외처리
        if (articleDatas.isEmpty()) {
            throw new ProviderResponseException(ProviderApiErrorCode.DATA_NOT_FOUND, "키워드에 적합한 결과가 없습니다.");
        }

        List<ListCardItem> listCardItems = Lists.newArrayList();

        articleDatas.subList(0, 5).forEach(articleData -> listCardItems.add(ListCardItem.builder()
                .title(articleData.getSubject())
                .description(articleData.getContent().length() > 9 ? articleData.getContent().substring(0, 10) : articleData.getContent())
                .imageUrl(articleData.getImage())
                .link(Stream.of(new String[][]{{"web", articleData.getUrl()}})
                        .collect(Collectors.toMap(data -> data[0], data -> data[1]))
                )
                .build()));

        ListCard listCard = ListCard.builder()
                .header(ListCardHeader.builder()
                        .title("검색 결과입니다.")
                        .imageUrl("")
                        .build())
                .items(listCardItems)
//                .buttons()
                .build();

        List<Map<String, ListCard>> outputs = Lists.newArrayList();

        Map<String, ListCard> map = new HashMap<>();

        map.put("listCard", listCard);

        outputs.add(map);

        KakaoResponseTemplate<ListCard> kakaoResponseTemplate = KakaoResponseTemplate.<ListCard>builder()
                .outputs(outputs)
                .build();

        KakaoResponse<ListCard> kakaoResponse = KakaoResponse.<ListCard>builder()
                .version("1.0")
                .template(kakaoResponseTemplate)
                .build();
//        KeywordResultData keywordResultData = KeywordResultData.builder()
//                .articleList(articleDatas)
//                .todayHighestPrice(todayHightestPrice)
//                .todayLowestPrice(todayLowestPrice)
//                .thisWeekHighestPrice(thisWeekHighestPrice)
//                .thisWeekLowestPrice(thisWeekLowestPrice)
//                .build();
        Gson gson = new Gson();

        // Redis에 키워드에 결과 저장 (만료시간 1시간)
        redisTemplate.opsForValue().set("KAKAO_" + keyword, gson.toJson(kakaoResponse), 60, TimeUnit.MINUTES);

        log.info(gson.toJson(kakaoResponse));

        return kakaoResponse;
    }

    private KeywordResultData searchResult(String keyword, List<ArticleData> articleDatas) {

        // 키워드에 대한 결과가 없을 경우 예외처리
        if (articleDatas.isEmpty()) {
            throw new ProviderResponseException(ProviderApiErrorCode.DATA_NOT_FOUND, "키워드에 적합한 결과가 없습니다.");
        }

        // 하루 전 데이터부터 지금까지 (24시간 데이터)
        Long yesterday = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1).toInstant().getEpochSecond();

        System.out.println(yesterday);

        // 24시간 기준 최고가, 최저가
        Long todayHightestPrice = articleDatas.stream().filter(articleData -> articleData.getPostingDtime() >= yesterday).map(ArticleData::getPrice).max(Long::compareTo).orElse(0L);
        Long todayLowestPrice = articleDatas.stream().filter(articleData -> articleData.getPostingDtime() >= yesterday).map(ArticleData::getPrice).min(Long::compareTo).orElse(0L);

        // 1주일 기준 최고가, 최저가 (List에는 1주일동안의 데이터만 존재함)
        Long thisWeekHighestPrice = articleDatas.stream().map(ArticleData::getPrice).max(Long::compareTo).orElse(0L);
        Long thisWeekLowestPrice = articleDatas.stream().map(ArticleData::getPrice).min(Long::compareTo).orElse(0L);

        double thisWeekAvgPrice = articleDatas.stream()
                .mapToDouble(ArticleData::getPrice)
                .average()
                .orElse(0);

        double todayAvgPrice = articleDatas.stream()
                .filter(articleData -> articleData.getPostingDtime() >= yesterday)
                .mapToDouble(ArticleData::getPrice)
                .average()
                .orElse(0);

        KeywordResultData keywordResultData = KeywordResultData.builder()
                .articleList(articleDatas)
                .todayHighestPrice(todayHightestPrice)
                .todayLowestPrice(todayLowestPrice)
                .todayAvgPrice(Math.floor(todayAvgPrice))
                .thisWeekHighestPrice(thisWeekHighestPrice)
                .thisWeekLowestPrice(thisWeekLowestPrice)
                .thisWeekAvgPrice(Math.floor(thisWeekAvgPrice))
                .build();

        popularRankingService.countingPopularRank(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.HOURS), keyword);

        redisTemplate.opsForValue().set(keyword, keywordResultData,60, TimeUnit.MINUTES);

        return keywordResultData;
    }
}
