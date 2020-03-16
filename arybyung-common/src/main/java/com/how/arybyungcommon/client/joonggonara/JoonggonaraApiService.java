package com.how.arybyungcommon.client.joonggonara;

import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraArticleDetailResponse;
import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraListResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface JoonggonaraApiService {

    @Headers({
            "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36",
            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
    })
    @GET("cafe-web/cafe2/ArticleList.json")
    Call<JoonggonaraListResponse> getArticleList(@Header("Cookie") String cookies,
                                                 @Query("search.clubid") String cafeId,
                                                 @Query("search.queryType") String type,
                                                 @Query("search.page") Long page);

    @Headers({
            "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36",
            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
    })
    @GET("cafe-web/cafe-articleapi/cafes/joonggonara/articles/{articleId}")
    Call<JoonggonaraArticleDetailResponse> getArticleDetail(@Header("Cookie") String cookies,
                                                            @Path("articleId") Long articleId,
                                                            @Query("useCafeId") Boolean useCafeId);
}
