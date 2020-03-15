package com.how.arybyungobserver.client.joonggonara;

import com.how.arybyungobserver.client.joonggonara.model.JoonggonaraListResponse;
import com.how.arybyungobserver.properties.UrlProperties;
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
    private final String cookie = "NNB=KVQ66TO4GI6V4; NRTK=ag#all_gr#1_ma#-2_si#0_en#0_sp#0; _ga=GA1.2.437056771.1583222171; page_uid=UEUGVdprvxZssnlRP1KssssstC8-419979; nid_inf=-1221235691; NID_AUT=ru1Alu9Ef0m8GVSKCuwaymigoR42EwJzjX0PbP7ef0mJo1x+pHv+qDwmw+TY/Sdb; NID_JKL=TB+DXHl13XhF7vH2z48CZb0+ab+j0KPqtjZe3oxWEdk=; BMR=s=1584088799248&r=https%3A%2F%2Fm.blog.naver.com%2FPostView.nhn%3FblogId%3Dwhdals0%26logNo%3D220494329022%26proxyReferer%3Dhttps%253A%252F%252Fwww.google.com%252F&r2=https%3A%2F%2Fwww.google.com%2F; MM_NEW=1; NFS=2; NID_SES=AAABp3nkdbGYxalcfuuJl6FkWbN+jrtm5azGxxOrB7EsEOuMarxIR6OeGfJXvdc0V9xXpmufvy5/x1mInbVHtda5ERRtyoGGNKR3xPhW41ZtXqdY4nEY7KiOeO00m548H7moJ3D+Ncf0jepXEjM4CmDEXSW/kwLFpImTtcjQ8rnO7CVH6FRN4csR24YyKRNZTraYfMZfgZ12gyNOjo9w1KLZbahpYlpo2QPfIEKNGoBWAoRWoEjjF8EIg0Kkc55OaINEMkYNS6wg4AyuUjF8Yid8dlWTUtFXASCqzHy49q7VGu/gLQpSGJMLaiCoo1EcpHSnCSCUtlhxqgcFYxRuq4R1x+ja3DuXvf+nUm07G/DG/T84PU8q3+EGRh05Cv+AIQXH1j+46Jdb5PzmF/tAwZ+U0l89WxZYaiDI1xWWHJPMYkEO+burgU+IaST+yfCNuByup6M8ohbQ7hv65tI7+/m4vrKnyjzztTixXShjH3skH9+tTH4voUezyG5clnaJ1oGJbXxSgeACjwhDS3J1nsbHvFXiCCV3jnif1xjdXjQln1F6vZsaw1AcC7IkBG40t/iSUg==";

    private final JoonggonaraApiService joonggonaraApiService;
    private final UrlProperties urlProperties;

    public JoonggonaraApiClient(UrlProperties urlProperties) {
        this.urlProperties = urlProperties;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlProperties.getJoonggoApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.joonggonaraApiService = retrofit.create(JoonggonaraApiService.class);
    }

    public Call<JoonggonaraListResponse> getArticleList() {
        return joonggonaraApiService.getArticleList(cookie, joonggonaraCafeId, "lastArticle", 1L);
    }


}
