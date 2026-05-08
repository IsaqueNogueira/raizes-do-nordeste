package com.raizesdonordeste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.raizesdonordeste.domain.repository")
public class RaizesDoNordesteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaizesDoNordesteApplication.class, args);
    }
}