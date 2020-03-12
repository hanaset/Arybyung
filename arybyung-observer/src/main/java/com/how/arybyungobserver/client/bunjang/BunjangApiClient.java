package com.how.arybyungobserver.client.bunjang;

import com.how.arybyungobserver.client.bunjang.model.BunjangHomeResponse;
import com.how.arybyungobserver.client.bunjang.model.BunjangItemResponse;
import com.how.arybyungobserver.properties.UrlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Slf4j
@Component
public class BunjangApiClient {

    private final BunjangApiService bunjangApiService;
    private final UrlProperties urlProperties;

    public BunjangApiClient(UrlProperties urlProperties) {
        this.urlProperties = urlProperties;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlProperties.getBunjangApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.bunjangApiService = retrofit.create(BunjangApiService.class);
    }

    public Call<BunjangItemResponse> getArticle(Long articleId) {
        return bunjangApiService.getArticle(articleId);
    }

    public Call<BunjangHomeResponse> getArticleList() {
        return bunjangApiService.getArticleList();
    }
}
