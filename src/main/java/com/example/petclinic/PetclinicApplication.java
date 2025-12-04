package com.example.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * PetclinicApplication is the Spring Boot entry point for the demo application.
 * It starts the embedded web server and auto-configures Spring components.
 */
@SpringBootApplication
public class PetclinicApplication {

    // PUBLIC_INTERFACE
    public static void main(String[] args) {
        /**
         * This is the public entry point of the application.
         * It boots Spring and starts the server.
         */
        SpringApplication.run(PetclinicApplication.class, args);
    }
}
