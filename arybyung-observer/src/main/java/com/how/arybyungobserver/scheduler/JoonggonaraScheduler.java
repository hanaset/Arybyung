package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.JoongnaraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoonggonaraScheduler {

    private final JoongnaraService joongnaraService;

    public JoonggonaraScheduler(JoongnaraService joongnaraService) {
        this.joongnaraService = joongnaraService;
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void parsing() {
        joongnaraService.parsingArticle();
    }
}
