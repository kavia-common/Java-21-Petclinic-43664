package com.marcoslombog.mybank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * OpenApiDynamicConfig provides an OpenAPI bean and an OpenApiCustomizer that sets the "servers"
 * list dynamically per request. The public base URL is resolved using ServerResolver, which
 * prefers APP_PUBLIC_BASE_URL and falls back to X-Forwarded-* headers.
 *
 * Swagger UI will consume relative config-url so that the UI itself loads correctly in
 * proxied environments. The servers entry helps "Try it out" target the correct host.
 */
@Configuration
public class OpenApiDynamicConfig {

    private final ServerResolver serverResolver;

    public OpenApiDynamicConfig(ServerResolver serverResolver) {
        this.serverResolver = serverResolver;
    }

    // PUBLIC_INTERFACE
    /**
     * Base OpenAPI document with Info block. Servers are set by the customizer below so that
     * they can be resolved per request context.
     */
    @Bean
    public OpenAPI myBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Bank API")
                        .version("2.0.0")
                        .description("Java 21, Spring Boot 3.x REST API for bank accounts.")
                        .contact(new Contact().name("My Bank Team")));
    }

    // PUBLIC_INTERFACE
    /**
     * Adds a dynamic servers list to the OpenAPI model using the current HTTP request.
     * If there is no request (e.g., during app startup), servers are left unset and
     * Swagger UI relative config will still operate.
     */
    @Bean
    public OpenApiCustomizer dynamicServersCustomizer() {
        return openApi -> {
            HttpServletRequest request = currentRequest();
            if (request == null) {
                return; // No-op outside request context
            }
            String baseUrl = serverResolver.resolveBaseUrl(request);
            openApi.setServers(List.of(new Server().url(baseUrl).description("Resolved API server")));
        };
    }

    private HttpServletRequest currentRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        Object req = attrs.getAttribute("org.springframework.web.context.request.ServletRequestAttributes.REQUEST", 0);
        if (req instanceof HttpServletRequest) {
            return (HttpServletRequest) req;
        }
        // Fallback for typical usage via RequestContextHolder
        try {
            return (HttpServletRequest) Class
                    .forName("org.springframework.web.context.request.ServletRequestAttributes")
                    .getMethod("getRequest")
                    .invoke(attrs);
        } catch (Exception ignored) {
            return null;
        }
    }
}
