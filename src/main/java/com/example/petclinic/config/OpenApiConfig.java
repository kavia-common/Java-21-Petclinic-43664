package com.example.petclinic.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
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
 * OpenApiConfig configures the OpenAPI metadata for the Petclinic API and explicitly sets the server URL
 * so that Swagger UI 'Try it out' calls hit the correct https endpoint with the expected port.
 *
 * Configuration/override:
 * - Property: petclinic.openapi.server-url
 * - Env var: PETCLINIC_OPENAPI_SERVER_URL
 * If none are provided, defaults to:
 *   https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002
 */
@Configuration
public class OpenApiConfig {

    /**
     * Reads the server URL from the property or environment variable, if provided.
     * If blank, a secure default is used to avoid mixed-content issues.
     */
    @Value("${petclinic.openapi.server-url:${PETCLINIC_OPENAPI_SERVER_URL:}}")
    private String configuredServerUrl;

    // PUBLIC_INTERFACE
    @Bean
    public OpenAPI petclinicOpenAPI() {
        // Default to the deployment URL with https scheme and port 3002
        String defaultUrl = "https://vscode-internal-29701-beta.beta01.cloud.kavia.ai:3002";
        String serverUrl = (configuredServerUrl == null || configuredServerUrl.isBlank())
                ? defaultUrl
                : configuredServerUrl;

        // Force https to avoid mixed-content errors if an http URL is mistakenly provided
        if (serverUrl.startsWith("http://")) {
            serverUrl = serverUrl.replaceFirst("http://", "https://");
        }

        Server server = new Server()
                .url(serverUrl)
                .description("Primary API server");

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
                .servers(List.of(server))
                .externalDocs(new ExternalDocumentation()
                        .description("Spring Petclinic Reference")
                        .url("https://github.com/spring-projects/spring-petclinic")
                );
    }
}
