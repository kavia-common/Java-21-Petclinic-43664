package com.marcoslombog.mybank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * OpenApiConfig configures the OpenAPI metadata for the my-bank API and allows overriding the server URL.
 * Configuration:
 * - Property: mybank.openapi.server-url
 * - Env var: MYBANK_OPENAPI_SERVER_URL
 */
@Configuration
public class OpenApiConfig {

    @Value("${mybank.openapi.server-url:${MYBANK_OPENAPI_SERVER_URL:}}")
    private String configuredServerUrl;

    // PUBLIC_INTERFACE
    @Bean
    public OpenAPI myBankOpenAPI() {
        String defaultUrl = "http://localhost:8080";
        String serverUrl = (configuredServerUrl == null || configuredServerUrl.isBlank())
                ? defaultUrl
                : configuredServerUrl;

        Server server = new Server().url(serverUrl).description("API server");
        return new OpenAPI()
                .info(new Info()
                        .title("My Bank API")
                        .version("2.0.0")
                        .description("Java 21, Spring Boot 3.x REST API for bank accounts.")
                        .contact(new Contact().name("My Bank Team")))
                .servers(List.of(server));
    }
}
