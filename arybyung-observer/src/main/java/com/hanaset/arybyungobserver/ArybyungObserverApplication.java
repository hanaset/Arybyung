package com.hanaset.arybyungobserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArybyungObserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArybyungObserverApplication.class, args);
    }

}
