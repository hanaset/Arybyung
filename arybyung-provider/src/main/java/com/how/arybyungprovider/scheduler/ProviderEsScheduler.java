package com.how.arybyungprovider.scheduler;

import com.how.arybyungprovider.service.ProviderEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProviderEsScheduler {

    private final ProviderEsService providerEsService;

    public ProviderEsScheduler(ProviderEsService providerEsService){
        this.providerEsService = providerEsService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void deleteBeforeWeekData() {

        log.info("Elastic Search Last Data Delete Start");
        providerEsService.deleteBeforeWeekData();
        log.info("Elastic Search Last Data Delete End");
    }
}
