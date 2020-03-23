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

    @Bean(name = "joonggonaraTaskExecutor")
    public ThreadPoolTaskExecutor joonggonaraTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("joonggonaraTaskExecutor-");
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);

        return taskExecutor;
    }

    @Bean(name = "danggnMarketTaskExecutor")
    public ThreadPoolTaskExecutor danggnMarketTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("danggnMarketTaskExecutor-");
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);

        return taskExecutor;
    }

    @Bean(name = "bunjangTaskExecutor")
    public ThreadPoolTaskExecutor bunjangTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("bunjangTaskExecutor-");
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);

        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
