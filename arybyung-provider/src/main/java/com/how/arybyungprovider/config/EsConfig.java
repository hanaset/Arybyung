package com.how.arybyungprovider.config;

import lombok.Getter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Getter
@Configuration
@PropertySource("classpath:properties/elasticsearch-${spring.profiles.active}.properties")
@EnableElasticsearchRepositories(basePackages = "com.how.muchcommon.repository.elasticrepository")
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.index}")
    private String esIndex;

    @Value("${elasticsearch.type}")
    private String type;

    @Bean
    RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(esHost)));

    }
}
