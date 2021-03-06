package com.how.arybyungcommon.client.bunjang;

import com.how.arybyungcommon.client.bunjang.model.BunjangHomeResponse;
import com.how.arybyungcommon.client.bunjang.model.BunjangItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BunjangApiService {

    @GET("product/{articleId}/detail_info.json")
    Call<BunjangItemResponse> getArticle(@Path("articleId") Long articleId);

    @GET("home/product/popular.json")
    Call<BunjangHomeResponse> getArticleList();
}
