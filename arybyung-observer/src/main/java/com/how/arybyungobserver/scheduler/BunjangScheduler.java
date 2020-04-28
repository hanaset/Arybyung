package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.ObserverControlService;
import com.how.arybyungobserver.service.bunjang.BunjangCrawlingService;
import com.how.arybyungobserver.service.bunjang.BunjangService;
import com.how.muchcommon.model.type.MarketName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BunjangScheduler {

    private final BunjangService bunjangService;
    private final ObserverControlService observerControlService;
    private final BunjangCrawlingService bunjangCrawlingService;

    public BunjangScheduler(BunjangService bunjangService,
                            BunjangCrawlingService bunjangCrawlingService,
                            ObserverControlService observerControlService) {
        this.bunjangService = bunjangService;
        this.observerControlService = observerControlService;
        this.bunjangCrawlingService = bunjangCrawlingService;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void parsing() {

        if(!observerControlService.checkSite(MarketName.bunjang.getName()))
            return;

        bunjangService.parsingArticle();
    }

    @Scheduled(cron = "59 59 * * * *", zone = "Asia/Seoul")
    public void saveCount() {
        bunjangService.saveCount();
    }

    @Scheduled(cron = "1 0 0 * * *", zone = "Asia/Seoul")
    public void initCount() {bunjangCrawlingService.init();}
}
