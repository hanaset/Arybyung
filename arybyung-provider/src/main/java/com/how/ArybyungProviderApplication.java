package com.how;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableJpaRepositories(basePackages = "com.how.muchcommon.repository.jparepository")
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ArybyungProviderApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ArybyungProviderApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run();
    }
}
