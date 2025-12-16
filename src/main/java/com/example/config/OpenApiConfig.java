package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * PUBLIC_INTERFACE
 * OpenAPI configuration to customize metadata shown in Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    /**
     * PUBLIC_INTERFACE
     * Builds the OpenAPI specification metadata.
     * @return OpenAPI with title, description and version
     */
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Java-21-Petclinic-43664 - Account API (my-bank aligned)")
                        .description("This service mirrors the my-bank account REST API using Java 21, H2, and Spring Boot 3. Swagger UI available at /swagger-ui.html.")
                        .version("1.0.0"));
    }
}
