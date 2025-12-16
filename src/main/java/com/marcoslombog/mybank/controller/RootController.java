package com.marcoslombog.mybank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * RootController exposes a simple landing endpoint at "/".
 * This provides a friendly JSON message for root requests often used by preview systems.
 */
@RestController
@Tag(name = "Root", description = "Root landing endpoint")
public class RootController {

    // PUBLIC_INTERFACE
    @GetMapping("/")
    @Operation(summary = "Root landing", description = "Returns a simple message with pointers to useful endpoints.")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("service", "mybank");
        body.put("message", "Welcome to my-bank API");
        body.put("endpoints", new String[] {
                "/healthz",
                "/actuator/health",
                "/accounts/all",
                "/swagger-ui.html"
        });
        return ResponseEntity.ok(body);
    }
}
