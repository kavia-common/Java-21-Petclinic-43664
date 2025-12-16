package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * The main Spring Boot application entry point for the Java-21-Petclinic-43664 service.
 * Scans the com.example package hierarchy including com.example.mybank for the aligned account API.
 */
@SpringBootApplication
public class Petclinic43664Application {

    /**
     * PUBLIC_INTERFACE
     * Bootstraps the Spring application.
     * @param args JVM/CLI arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Petclinic43664Application.class, args);
    }
}
