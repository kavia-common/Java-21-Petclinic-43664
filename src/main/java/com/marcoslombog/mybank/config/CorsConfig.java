package com.marcoslombog.mybank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * PUBLIC_INTERFACE
 * CorsConfig provides a basic CORS setup to support browser-based clients such as Swagger UI.
 * By default, it allows the same-origin (http://localhost:3002 or the deployed host).
 * You can override allowed origins with the ALLOWED_ORIGINS environment variable (comma-separated list).
 */
@Configuration
public class CorsConfig {

    private static final String DEFAULT_ALLOWED_ORIGINS = ""; // empty -> same-origin

    // PUBLIC_INTERFACE
    /**
     * Configures CORS for the application.
     * Allowed origins can be set via ALLOWED_ORIGINS environment variable (comma-separated).
     * Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS
     * Headers: Content-Type, Authorization
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        String allowed = System.getenv("ALLOWED_ORIGINS");
        final String[] allowedOrigins = StringUtils.hasText(allowed)
                ? StringUtils.commaDelimitedListToStringArray(allowed)
                : (StringUtils.hasText(DEFAULT_ALLOWED_ORIGINS)
                    ? StringUtils.commaDelimitedListToStringArray(DEFAULT_ALLOWED_ORIGINS)
                    : new String[0]); // empty -> same-origin only

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                var mapping = registry.addMapping("/**")
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                    .allowedHeaders("Content-Type", "Authorization")
                    .allowCredentials(false)
                    .maxAge(3600);
                if (allowedOrigins.length > 0) {
                    mapping.allowedOrigins(allowedOrigins);
                } // else same-origin default applies
            }
        };
    }
}
