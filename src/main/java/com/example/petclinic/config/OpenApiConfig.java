package com.example.petclinic.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PUBLIC_INTERFACE
 * OpenApiConfig configures the OpenAPI metadata for the Petclinic API.
 */
@Configuration
public class OpenApiConfig {

    // PUBLIC_INTERFACE
    @Bean
    public OpenAPI petclinicOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Petclinic API")
                .version("1.0.0")
                .description("Java 21 Petclinic-style demo API with Swagger UI and Actuator")
                .contact(new Contact()
                    .name("Petclinic Team")
                    .url("https://spring.io/projects/spring-petclinic")
                )
            )
            .externalDocs(new ExternalDocumentation()
                .description("Spring Petclinic Reference")
                .url("https://github.com/spring-projects/spring-petclinic")
            );
    }
}
