package com.hanaset.arybyungobserver.scheduler;

import com.hanaset.arybyungobserver.client.DanggnMarketParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DanggnMarketScheduler {

    private final DanggnMarketParser danggnMarketParser;

    public DanggnMarketScheduler(DanggnMarketParser danggnMarketParser) {
        this.danggnMarketParser = danggnMarketParser;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void test() throws IOException {
        danggnMarketParser.getArticle(54604802L);
    }
}
