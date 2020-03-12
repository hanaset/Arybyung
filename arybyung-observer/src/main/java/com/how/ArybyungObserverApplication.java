package com.how;

import com.how.arybyungobserver.client.DriverConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ArybyungObserverApplication {

    public static void main(String[] args) {

        System.setProperty("webdriver.gecko.driver", DriverConstants.DRIVER_PATH);
//        System.setProperty("webdriver.gecko.driver", ParserConstants.TEST_DRIVER_PATH); //테스트코드
        System.setProperty("java.awt.headless", "false");

        SpringApplication.run(ArybyungObserverApplication.class, args);
    }
}
