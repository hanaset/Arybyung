package com.how.arybyungprovider.scheduler;

import com.how.arybyungprovider.service.ProviderEsService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;

@ComponentScan
public class ProviderEsScheduler {

    private final ProviderEsService providerEsService;

    public ProviderEsScheduler(ProviderEsService providerEsService){
        this.providerEsService = providerEsService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void deleteBeforeWeekData() {
        providerEsService.deleteBeforeWeekData();
    }
}
