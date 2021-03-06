package com.how.arybyungcommon.client.joonggonara;

import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraArticleDetailResponse;
import com.how.arybyungcommon.client.joonggonara.model.JoonggonaraListResponse;
import com.how.arybyungcommon.properties.UrlProperties;
import com.how.muchcommon.model.type.MarketName;
import com.how.muchcommon.repository.jparepository.CookieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Slf4j
@Component
public class JoonggonaraApiClient {

    private final String joonggonaraCafeId = "10050146";
    private final String cookie;
    private final JoonggonaraApiService joonggonaraApiService;
    private final UrlProperties urlProperties;
    private final CookieRepository cookieRepository;

    public JoonggonaraApiClient(UrlProperties urlProperties,
                                CookieRepository cookieRepository) {
        this.urlProperties = urlProperties;
        this.cookieRepository = cookieRepository;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlProperties.getJoonggoApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.cookie = cookieRepository.findBySite(MarketName.joonggonara.getName()).getValue();
        this.joonggonaraApiService = retrofit.create(JoonggonaraApiService.class);

        log.info("Joonggonara Cookie : {}", this.cookie);
    }

    public Call<JoonggonaraListResponse> getArticleList() {
        return joonggonaraApiService.getArticleList(cookie, joonggonaraCafeId, "lastArticle", 1L);
    }

    public Call<JoonggonaraArticleDetailResponse> getArticleDetail(Long articleId) {
        return joonggonaraApiService.getArticleDetail(cookie, articleId, false);
    }


}
