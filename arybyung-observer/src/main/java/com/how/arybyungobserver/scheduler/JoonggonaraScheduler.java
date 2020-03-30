package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.ObserverControlService;
import com.how.arybyungobserver.service.joonggonara.JoonggonaraCrawlingService;
import com.how.arybyungobserver.service.joonggonara.JoongnaraService;
import com.how.muchcommon.model.type.MarketName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoonggonaraScheduler {

    private final JoongnaraService joongnaraService;
    private final JoonggonaraCrawlingService joonggonaraCrawlingService;
    private final ObserverControlService observerControlService;

    public JoonggonaraScheduler(JoongnaraService joongnaraService,
                                JoonggonaraCrawlingService joonggonaraCrawlingService,
                                ObserverControlService observerControlService) {
        this.joongnaraService = joongnaraService;
        this.joonggonaraCrawlingService = joonggonaraCrawlingService;
        this.observerControlService = observerControlService;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void parsing() {

        if (!observerControlService.checkSite(MarketName.joonggonara.getName()))
            return;

        joongnaraService.parsingArticle();
    }

    @Scheduled(cron = "59 59 * * * *", zone = "Asia/Seoul")
    public void saveCount() {
        joongnaraService.saveCount();
    }

    @Scheduled(cron = "1 0 0 * * *", zone = "Asia/Seoul")
    public void initCount() { joonggonaraCrawlingService.init();}
}
