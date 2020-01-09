package com.how;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ArybyungObserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArybyungObserverApplication.class, args);
    }

    @Bean
    public TaskExecutor joonggonaraTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("joonggonaraTaskExecutor-");
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(5);

        return taskExecutor;
    }

    @Bean
    public TaskExecutor danggnMarketTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("danggnMarketTaskExecutor-");
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(3);

        return taskExecutor;
    }

}
