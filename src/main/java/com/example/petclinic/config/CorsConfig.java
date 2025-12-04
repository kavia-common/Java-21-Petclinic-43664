package com.example.petclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * PUBLIC_INTERFACE
 * CorsConfig enables CORS for the deployment origin to ensure Swagger UI 'Try it out'
 * requests are not blocked by the browser.
 *
 * Allowed origin(s):
 * - https://vscode-internal-29701-beta.beta01.cloud.kavia.ai
 * - https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002
 *
 * Methods: GET, POST, PUT, DELETE, OPTIONS
 * Headers: Content-Type, Authorization
 */
@Configuration
public class CorsConfig {

    private static final String ORIGIN_NO_PORT = "https://vscode-internal-29701-beta.beta01.cloud.kavia.ai";
    private static final String ORIGIN_WITH_PORT = "https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002";

    // PUBLIC_INTERFACE
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // Using a bean to configure CORS globally for all endpoints
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins(ORIGIN_NO_PORT, ORIGIN_WITH_PORT)
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("Content-Type", "Authorization")
                    .allowCredentials(false)
                    .maxAge(3600);
            }
        };
    }
}
