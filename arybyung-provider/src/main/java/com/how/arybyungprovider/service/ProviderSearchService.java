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
            redisTemplate.opsForValue().set(keyword, result, 10, TimeUnit.MINUTES);
            providerArticleService.validationArticle(keyword, result);
            return result;
        } else {
            popularRankingService.countingPopularRank(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.HOURS), keyword);
            return redisTemplate.opsForValue().get(keyword);
        }
    }

    //    @Deprecated
//    public KakaoResponse<?> kakaoSearchKeyword(String keyword) {
//        Gson gson = new Gson();
//        if (!redisTemplate.hasKey("KAKAO_" + keyword)) {
//            return openBuilderResult(keyword, providerEsService.searchKeyword(keyword));
//        } else {
//            // return mapper.readValue((String) Objects.requireNonNull(redisTemplate.opsForValue().get(keyword)), KakaoResponse.class);
//            return (KakaoResponse<?>) gson.fromJson((String) redisTemplate.opsForValue().get("KAKAO_" + keyword), KakaoResponse.class);
//        }
//    }

//    @Deprecated
//    private KakaoResponse<?> openBuilderResult(String keyword, List<ArticleData> articleDatas) {
//
//        // 키워드에 대한 결과가 없을 경우 예외처리
//        if (articleDatas.isEmpty()) {
//            throw new ProviderResponseException(ProviderApiErrorCode.DATA_NOT_FOUND, "키워드에 적합한 결과가 없습니다.");
//        }
//
//        List<ListCardItem> listCardItems = Lists.newArrayList();
//
//        articleDatas.subList(0, 5).forEach(articleData -> listCardItems.add(ListCardItem.builder()
//                .title(articleData.getSubject())
//                .description(articleData.getContent().length() > 9 ? articleData.getContent().substring(0, 10) : articleData.getContent())
//                .imageUrl(articleData.getImage())
//                .link(Stream.of(new String[][]{{"web", articleData.getUrl()}})
//                        .collect(Collectors.toMap(data -> data[0], data -> data[1]))
//                )
//                .build()));
//
//        ListCard listCard = ListCard.builder()
//                .header(ListCardHeader.builder()
//                        .title("검색 결과입니다.")
//                        .imageUrl("")
//                        .build())
//                .items(listCardItems)
////                .buttons()
//                .build();
//
//        List<Map<String, ListCard>> outputs = Lists.newArrayList();
//
//        Map<String, ListCard> map = new HashMap<>();
//
//        map.put("listCard", listCard);
//
//        outputs.add(map);
//
//        KakaoResponseTemplate<ListCard> kakaoResponseTemplate = KakaoResponseTemplate.<ListCard>builder()
//                .outputs(outputs)
//                .build();
//
//        KakaoResponse<ListCard> kakaoResponse = KakaoResponse.<ListCard>builder()
//                .version("1.0")
//                .template(kakaoResponseTemplate)
//                .build();
////        KeywordResultData keywordResultData = KeywordResultData.builder()
////                .articleList(articleDatas)
////                .todayHighestPrice(todayHightestPrice)
////                .todayLowestPrice(todayLowestPrice)
////                .thisWeekHighestPrice(thisWeekHighestPrice)
////                .thisWeekLowestPrice(thisWeekLowestPrice)
////                .build();
//        Gson gson = new Gson();
//
//        // Redis에 키워드에 결과 저장 (만료시간 1시간)
//        redisTemplate.opsForValue().set("KAKAO_" + keyword, gson.toJson(kakaoResponse), 60, TimeUnit.MINUTES);
//
//        log.info(gson.toJson(kakaoResponse));
//
//        return kakaoResponse;
//    }
}
