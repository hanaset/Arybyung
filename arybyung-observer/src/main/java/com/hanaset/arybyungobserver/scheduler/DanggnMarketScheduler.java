package com.hanaset.arybyungobserver.scheduler;

import com.hanaset.arybyungobserver.service.DanggnMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DanggnMarketScheduler {

    private final DanggnMarketService danggnMarketService;

    public DanggnMarketScheduler(DanggnMarketService danggnMarketService) {
        this.danggnMarketService = danggnMarketService;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void parsing() throws IOException {
        log.info("======= danggnMarket Parsing Start =======");
        danggnMarketService.parsingArticle();
        log.info("======= danggnMarket Parsing end =======");
    }
}
