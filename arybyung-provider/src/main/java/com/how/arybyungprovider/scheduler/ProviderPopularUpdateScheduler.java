package com.how.arybyungprovider.scheduler;

import com.how.arybyungprovider.service.PopularRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderPopularUpdateScheduler {

    private final PopularRankingService popularRankingService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void updatePopularChart() {
        popularRankingService.updatePopularChart();
    }

}
