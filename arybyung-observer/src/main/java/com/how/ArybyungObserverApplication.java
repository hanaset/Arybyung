package com.how;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ArybyungObserverApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(ArybyungObserverApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run();
    }
}
