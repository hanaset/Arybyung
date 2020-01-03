package com.hanaset.arybyungobserver.scheduler;

import com.hanaset.arybyungobserver.component.JoonggonaraParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoonggonaraScheduler {

    private final JoonggonaraParser joonggonaraParser;

    public JoonggonaraScheduler(JoonggonaraParser joonggonaraParser) {
        this.joonggonaraParser = joonggonaraParser;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void test() throws Exception{
        joonggonaraParser.naverLogin();
    }
}
