package com.how.arybyungobserver.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
public class SchedulerExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final ThreadPoolTaskExecutor joonggonaraTaskExecutor;

    public SchedulerExceptionHandler(@Qualifier(value = "joonggonaraTaskExecutor") ThreadPoolTaskExecutor joonggonaraTaskExecutor) {
        this.joonggonaraTaskExecutor = joonggonaraTaskExecutor;
    }

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        joonggonaraTaskExecutor.destroy();
    }
}
