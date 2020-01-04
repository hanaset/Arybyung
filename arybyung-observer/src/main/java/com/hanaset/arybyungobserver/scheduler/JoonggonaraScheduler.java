package com.hanaset.arybyungobserver.scheduler;

import com.hanaset.arybyungobserver.service.JoongnaraService;
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

    @Scheduled(fixedRate = 1000 * 60)
    public void test() throws Exception {
        joongnaraService.parsingArticle();
    }
}
