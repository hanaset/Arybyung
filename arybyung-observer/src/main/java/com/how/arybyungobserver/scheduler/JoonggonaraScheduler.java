package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.ObserverControlService;
import com.how.arybyungobserver.service.joonggonara.JoongnaraService;
import com.how.muchcommon.model.type.MarketName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoonggonaraScheduler {

    private final JoongnaraService joongnaraService;
    private final ObserverControlService observerControlService;

    public JoonggonaraScheduler(JoongnaraService joongnaraService,
                                ObserverControlService observerControlService) {
        this.joongnaraService = joongnaraService;
        this.observerControlService = observerControlService;
    }

    @Scheduled(fixedRate = 1000 * 10)
    public void parsing() {

        if(!observerControlService.checkSite(MarketName.joonggonara.getName()))
                return;

        joongnaraService.parsingArticle();
    }
}
