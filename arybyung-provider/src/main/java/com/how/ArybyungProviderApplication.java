package com.how;

import com.how.arybyungprovider.service.PopularRankingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableJpaRepositories(basePackages = "com.how.muchcommon.repository.jparepository")
@SpringBootApplication
@EnableScheduling
public class ArybyungProviderApplication {

    private final PopularRankingService popularRankingService;

    public ArybyungProviderApplication(PopularRankingService popularRankingService) {
        this.popularRankingService = popularRankingService;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ArybyungProviderApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run();
    }

    @PostConstruct
    public void generatePopularRank () {
        popularRankingService.updatePopularChart();
    }

}
