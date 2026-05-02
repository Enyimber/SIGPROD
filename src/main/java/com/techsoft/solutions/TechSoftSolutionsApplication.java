package com.techsoft.solutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TechSoftSolutionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechSoftSolutionsApplication.class, args);
    }
}
