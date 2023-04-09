package com.example.ece651;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Ece651Application {

    public static void main(String[] args) {
        SpringApplication.run(Ece651Application.class, args);
    }

}
