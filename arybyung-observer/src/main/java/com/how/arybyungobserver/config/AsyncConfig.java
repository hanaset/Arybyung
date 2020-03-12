package com.how.arybyungobserver.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    public ThreadPoolTaskExecutor joonggonaraTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("joonggonaraTaskExecutor-");
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setMaxPoolSize(20);

        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor danggnMarketTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("danggnMarketTaskExecutor-");
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setMaxPoolSize(20);

        return taskExecutor;
    }

    @Bean
    public ThreadPoolTaskExecutor bunjangTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("bunjangMarketTaskExecutor-");
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setMaxPoolSize(20);

        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
