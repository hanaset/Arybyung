package com.how.arybyungprovider.config;

import lombok.Getter;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@PropertySource("classpath:properties/elasticsearch-${spring.profiles.active}.properties")
@EnableElasticsearchRepositories(basePackages = "com.how.muchcommon.repository.elasticrepository")
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Getter
    @Value("${elasticsearch.index}")
    private String esIndex;

    @Bean
    RestHighLevelClient restHighLevelClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esHost)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
