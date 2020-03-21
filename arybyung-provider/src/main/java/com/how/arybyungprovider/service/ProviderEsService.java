package com.how.arybyungprovider.service;

import com.how.arybyungprovider.constant.ProviderApiErrorCode;
import com.how.arybyungprovider.exception.ProviderResponseException;
import com.how.arybyungprovider.model.ArticleData;
import com.how.arybyungprovider.model.KeywordResultData;
import com.how.muchcommon.config.EsConfig;
import com.how.muchcommon.entity.elasticentity.ArticleEsEntity;
import com.how.muchcommon.repository.elasticrepository.ArticleEsRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ProviderEsService {

    private final PopularRankingService popularRankingService;
    private final ArticleEsRepository articleEsRepository;
    private final RestHighLevelClient restHighLevelClient;
    private final EsConfig esConfig;

    public ProviderEsService(PopularRankingService popularRankingService,
                             ArticleEsRepository articleEsRepository,
                             RestHighLevelClient restHighLevelClient,
                             EsConfig esConfig) {
        this.popularRankingService = popularRankingService;
        this.articleEsRepository = articleEsRepository;
        this.restHighLevelClient = restHighLevelClient;
        this.esConfig = esConfig;
    }

    private List<ArticleData> searchKeyword(String keyword) {
        String keywordNumber = Long.toString(keyword.split(" ").length);
        MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(keyword, "subject").operator(Operator.OR).minimumShouldMatch(keywordNumber);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQueryBuilder)
                .withPageable(PageRequest.of(0, 100))
                .withSort(SortBuilders.fieldSort("posting_dtime").order(SortOrder.DESC));
        List<ArticleEsEntity> articleEsEntities = StreamSupport.stream(articleEsRepository.search(nativeSearchQueryBuilder.build()).spliterator(), false).collect(Collectors.toList());

        return articleEsEntities.stream().map(articleEsEntity ->
                ArticleData.builder()
                        .id(articleEsEntity.getId())
                        .articleId(articleEsEntity.getArticleId())
                        .subject(articleEsEntity.getSubject())
                        .content(articleEsEntity.getContent())
                        .price(articleEsEntity.getPrice())
                        .image(articleEsEntity.getImage())
                        .postingDtime(articleEsEntity.getPostingDtime().toInstant().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .site(articleEsEntity.getSite())
                        .url(articleEsEntity.getUrl())
                        .state(articleEsEntity.getState())
                        .updDtime(articleEsEntity.getUpdDtime().toInstant().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .build()
        ).collect(Collectors.toList());
    }

    public KeywordResultData searchResult(String keyword) {

        List<ArticleData> articleDatas = searchKeyword(keyword);

        // 키워드에 대한 결과가 없을 경우 예외처리
        if (articleDatas == null || articleDatas.isEmpty()) {
            throw new ProviderResponseException(ProviderApiErrorCode.DATA_NOT_FOUND, "키워드에 적합한 결과가 없습니다.");
        }

        final Comparator<ArticleData> comp = Comparator.comparingLong(ArticleData::getPrice);
        // 하루 전 데이터부터 지금까지 (24시간 데이터)
        Long yesterday = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1).toInstant().getEpochSecond();

        // 1주일 기준 최고가, 최저가 (List에는 1주일동안의 데이터만 존재함)
        ArticleData thisWeekHighestArticle = articleDatas.stream().max(comp).get();
        ArticleData thisWeekLowestArticle = articleDatas.stream().min(comp).get();

        // 24시간 기준 최고가, 최저가 (24시간기준 최고, 최저 구할 수 없으면 1주일 데이터로 대치)
        ArticleData todayHighestArticle = articleDatas.stream()
                .filter(articleData -> articleData.getPostingDtime() >= yesterday)
                .max(comp)
                .orElse(thisWeekHighestArticle);

        ArticleData todayLowestArticle = articleDatas.stream()
                .filter(articleData -> articleData.getPostingDtime() >= yesterday)
                .min(comp)
                .orElse(thisWeekLowestArticle);

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
                .todayHighestPrice(todayHighestArticle)
                .todayLowestPrice(todayLowestArticle)
                .todayAvgPrice(Math.floor(todayAvgPrice))
                .thisWeekHighestPrice(thisWeekHighestArticle)
                .thisWeekLowestPrice(thisWeekLowestArticle)
                .thisWeekAvgPrice(Math.floor(thisWeekAvgPrice))
                .validating(false)
                .build();

        popularRankingService.countingPopularRank(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).truncatedTo(ChronoUnit.HOURS), keyword);

        return keywordResultData;
    }

    public void deleteBeforeWeekData() {

        try {

            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).minusWeeks(1);

            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("posting_dtime").lt(zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            DeleteByQueryRequest request = new DeleteByQueryRequest(esConfig.getEsIndex());
            request.setQuery(queryBuilder);
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);

        } catch (IOException e) {
            log.error("DeleteByQuery IOException : {}", e.getMessage());
        }
    }

    public void deleteAllData() {
        articleEsRepository.deleteAll();
    }

    public void deleteInvalidArticle(List<Long> articleList) {

    }
}
