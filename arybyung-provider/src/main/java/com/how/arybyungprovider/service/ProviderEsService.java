package com.how.arybyungprovider.service;

import com.how.arybyungprovider.model.ArticleData;
import com.how.muchcommon.config.EsConfig;
import com.how.muchcommon.entity.elasticentity.ArticleEsEntity;
import com.how.muchcommon.model.type.FieldParam;
import com.how.muchcommon.model.type.OrderType;
import com.how.muchcommon.repository.elasticrepository.ArticleEsRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ProviderEsService {

    private final ArticleEsRepository articleEsRepository;
    private final RestHighLevelClient restHighLevelClient;
    private final EsConfig esConfig;

    public ProviderEsService(ArticleEsRepository articleEsRepository,
                             RestHighLevelClient restHighLevelClient,
                             EsConfig esConfig) {
        this.articleEsRepository = articleEsRepository;
        this.restHighLevelClient = restHighLevelClient;
        this.esConfig = esConfig;
    }

    public List<ArticleData> searchKeyword(String keyword, FieldParam field, OrderType order) {

        MultiMatchQueryBuilder matchAllQueryBuilder = new MultiMatchQueryBuilder(keyword, "subject", "content").operator(Operator.AND);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(matchAllQueryBuilder)
                .withSort(SortBuilders.fieldSort(field.getValue()).order(SortOrder.fromString(order.getValue())));

        List<ArticleEsEntity> articleEsEntities = StreamSupport.stream(articleEsRepository.search(nativeSearchQueryBuilder.build()).spliterator(), false).collect(Collectors.toList());

        return articleEsEntities.stream().map(articleEsEntity ->
                ArticleData.builder()
                        .articleId(articleEsEntity.getArticleId())
                        .subject(articleEsEntity.getSubject())
                        .content(articleEsEntity.getContent())
                        .price(articleEsEntity.getPrice())
                        .image(articleEsEntity.getImage())
                        .postingDtime(ZonedDateTime.from(articleEsEntity.getPostingDtime().toInstant().atZone(ZoneId.of("Asia/Seoul"))))
                        .site(articleEsEntity.getSite())
                        .url(articleEsEntity.getUrl())
                        .state(articleEsEntity.getState())
                        .build()
        ).collect(Collectors.toList());
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
}
