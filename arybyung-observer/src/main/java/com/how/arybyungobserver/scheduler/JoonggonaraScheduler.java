package com.how.arybyungobserver.scheduler;

import com.how.arybyungobserver.service.JoongnaraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoonggonaraScheduler {

    private final JoongnaraService joongnaraService;

    public JoonggonaraScheduler(JoongnaraService joongnaraService) {
        this.joongnaraService = joongnaraService;
    }

//    @Scheduled(fixedRate = 1000 * 60)
    public void parsing() throws Exception {
        log.info("======= Joonggonara Parsing Start =======");
        joongnaraService.parsingArticle();
        log.info("======= Joonggonara Parsing end =======");
    }
}
