package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.ObserverControlService;
import com.how.arybyungobserver.service.danggnmarket.DanggnMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DanggnMarketScheduler {

    private final DanggnMarketService danggnMarketService;
    private final ObserverControlService observerControlService;

    public DanggnMarketScheduler(DanggnMarketService danggnMarketService,
                                 ObserverControlService observerControlService) {
        this.danggnMarketService = danggnMarketService;
        this.observerControlService = observerControlService;
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void parsing() throws IOException {

        if(!observerControlService.checkSite("danggn"))
            return;

        danggnMarketService.parsingArticle();
    }
}
