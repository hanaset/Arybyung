package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.ObserverControlService;
import com.how.arybyungobserver.service.danggnmarket.DanggnCrawlingService;
import com.how.arybyungobserver.service.danggnmarket.DanggnMarketService;
import com.how.muchcommon.model.type.MarketName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DanggnMarketScheduler {

    private final DanggnMarketService danggnMarketService;
    private final DanggnCrawlingService danggnCrawlingService;
    private final ObserverControlService observerControlService;

    public DanggnMarketScheduler(DanggnMarketService danggnMarketService,
                                 DanggnCrawlingService danggnCrawlingService,
                                 ObserverControlService observerControlService) {
        this.danggnMarketService = danggnMarketService;
        this.danggnCrawlingService = danggnCrawlingService;
        this.observerControlService = observerControlService;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void parsing() throws IOException {

        if(!observerControlService.checkSite(MarketName.danggn.getName()))
            return;

        danggnMarketService.parsingArticle();
    }

    @Scheduled(cron = "59 59 * * * *", zone = "Asia/Seoul")
    public void saveCount() {
        danggnMarketService.saveCount();
    }

    @Scheduled(cron = "1 0 0 * * *", zone = "Asia/Seoul")
    public void initCount() { danggnCrawlingService.init();}
}
