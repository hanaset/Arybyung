package com.how;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ArybyungProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArybyungProviderApplication.class, args);
    }

}
