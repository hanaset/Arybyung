package com.how.arybyungobserver.properties;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:properties/url/url-${spring.profiles.active}.properties")
@Getter
@Configuration
public class UrlProperties {

    @Value("${url.joonggoArticleUrl}")
    private String joonggoArtlcleUrl;

    @Value("${url.joonggoArticleListUrl}")
    private String joonggoArticleListUrl;

    @Value("${url.danggnArticleUrl}")
    private String danggnArticleUrl;

    @Value("${url.bunjangArticleUrl}")
    private String bunjangArticleUrl;

    @Value("${url.bunjangApiUrl}")
    private String bunjangApiUrl;
}
