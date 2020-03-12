package com.how.arybyungobserver.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:properties/naver/naver-${spring.profiles.active}.properties")
@Getter
@Configuration
public class NaverLoginProperties {

    @Value("${naver.user_id}")
    private String userId;

    @Value("${naver.password}")
    private String password;

    @Value("${naver.url}")
    private String url;
}
