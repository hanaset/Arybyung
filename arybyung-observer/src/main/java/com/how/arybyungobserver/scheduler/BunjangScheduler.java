package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.bunjang.BunjangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BunjangScheduler {

    private final BunjangService bunjangService;

    public BunjangScheduler(BunjangService bunjangService) {
        this.bunjangService = bunjangService;
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void parsing() {
        bunjangService.parsingArticle();
    }
}
