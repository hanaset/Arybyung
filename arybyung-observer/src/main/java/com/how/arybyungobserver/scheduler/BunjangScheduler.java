package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.ObserverControlService;
import com.how.arybyungobserver.service.bunjang.BunjangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BunjangScheduler {

    private final BunjangService bunjangService;
    private final ObserverControlService observerControlService;

    public BunjangScheduler(BunjangService bunjangService,
                            ObserverControlService observerControlService) {
        this.bunjangService = bunjangService;
        this.observerControlService = observerControlService;
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void parsing() {

        if(!observerControlService.checkSite("bunjang"))
            return;
        bunjangService.parsingArticle();
    }
}
