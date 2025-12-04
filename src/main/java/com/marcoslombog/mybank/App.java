package com.marcoslombog.mybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * App is the Spring Boot entry point for the my-bank application.
 * It bootstraps the application, starts the embedded web server, and triggers component scanning for the mybank packages.
 */
@SpringBootApplication
public class App {

    // PUBLIC_INTERFACE
    public static void main(String[] args) {
        /**
         * Application entry point.
         * Starts the Spring Boot application.
         */
        SpringApplication.run(App.class, args);
    }
}
