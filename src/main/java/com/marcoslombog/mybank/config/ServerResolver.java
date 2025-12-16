package com.marcoslombog.mybank.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Optional;

/**
 * PUBLIC_INTERFACE
 * ServerResolver computes the publicly reachable base URL of the application.
 * Resolution order:
 * 1) APP_PUBLIC_BASE_URL environment property (or Spring property) if set.
 * 2) X-Forwarded-* headers (proto/host/port/prefix).
 * 3) Falls back to request scheme/host/port/contextPath.
 *
 * This is used to ensure OpenAPI "servers" advertises the correct hostname in Swagger UI,
 * especially behind reverse proxies and in preview environments.
 */
@Component
public class ServerResolver {

    private final String configuredBaseUrl;

    public ServerResolver() {
        // Read from environment variable or system property
        String env = System.getenv("APP_PUBLIC_BASE_URL");
        if (!StringUtils.hasText(env)) {
            env = System.getProperty("APP_PUBLIC_BASE_URL", "");
        }
        this.configuredBaseUrl = env != null ? env.trim() : "";
    }

    // PUBLIC_INTERFACE
    /**
     * Resolve the public base URL for a given request.
     * @param request current HTTP servlet request
     * @return a String base URL like "https://example.com:3002" (no trailing slash)
     */
    public String resolveBaseUrl(HttpServletRequest request) {
        // 1) Explicit override
        if (StringUtils.hasText(configuredBaseUrl)) {
            return stripTrailingSlash(configuredBaseUrl);
        }

        // 2) Use X-Forwarded headers if present
        String forwardedProto = headerOrNull(request, "X-Forwarded-Proto");
        String forwardedHost = headerOrNull(request, "X-Forwarded-Host");
        String forwardedPort = headerOrNull(request, "X-Forwarded-Port");
        String forwardedPrefix = headerOrNull(request, "X-Forwarded-Prefix");

        if (StringUtils.hasText(forwardedHost)) {
            String scheme = StringUtils.hasText(forwardedProto) ? forwardedProto : defaultScheme(request);
            String hostPort = forwardedHost;

            // If header lacks port and X-Forwarded-Port is present, append it (when not default for scheme)
            if (!hostPort.contains(":") && StringUtils.hasText(forwardedPort)) {
                if (!(scheme.equals("http") && "80".equals(forwardedPort))
                        && !(scheme.equals("https") && "443".equals(forwardedPort))) {
                    hostPort = hostPort + ":" + forwardedPort;
                }
            }

            String prefix = (StringUtils.hasText(forwardedPrefix)) ? ensureStartsWithSlash(forwardedPrefix) : "";
            return stripTrailingSlash(scheme + "://" + hostPort + prefix);
        }

        // 3) Fallback to request info
        String scheme = Optional.ofNullable(request.getScheme()).orElse("http");
        String serverName = Optional.ofNullable(request.getServerName()).orElse("localhost");
        int port = request.getServerPort();
        String contextPath = Optional.ofNullable(request.getContextPath()).orElse("");

        String hostPort = serverName;
        if (!(scheme.equals("http") && port == 80) && !(scheme.equals("https") && port == 443)) {
            hostPort = hostPort + ":" + port;
        }
        return stripTrailingSlash(scheme + "://" + hostPort + ensureStartsWithSlash(contextPath));
    }

    private static String headerOrNull(HttpServletRequest request, String name) {
        String v = request.getHeader(name);
        if (!StringUtils.hasText(v)) return null;
        // Some proxies may provide comma-separated values; use first
        return v.split(",")[0].trim();
    }

    private static String defaultScheme(HttpServletRequest request) {
        return Optional.ofNullable(request.getScheme()).filter(StringUtils::hasText).orElse("http");
    }

    private static String ensureStartsWithSlash(String path) {
        if (!StringUtils.hasText(path)) return "";
        return path.startsWith("/") ? path : "/" + path;
    }

    private static String stripTrailingSlash(String url) {
        if (url == null) return null;
        if (url.endsWith("/")) return url.substring(0, url.length() - 1);
        return url;
    }
}
