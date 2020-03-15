package com.how.arybyungobserver.client.joonggonara;

import com.how.arybyungobserver.client.joonggonara.model.JoonggonaraListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface JoonggonaraApiService {

    @Headers({
            "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36",
            "authority: apis.naver.com"
    })
    @GET("cafe-web/cafe2/ArticleList.json")
    Call<JoonggonaraListResponse> getArticleList(@Header("Cookie") String cookies,
                                                 @Query("search.clubid") String cafeId,
                                                 @Query("search.queryType") String type,
                                                 @Query("search.page") Long page);
}
