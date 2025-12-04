package com.example.petclinic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * PUBLIC_INTERFACE
 * HealthController exposes a simple /healthz endpoint returning 200 OK and a JSON payload.
 * Note: Actuator /actuator/health is also enabled for liveness/readiness checks.
 */
@RestController
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    // PUBLIC_INTERFACE
    @GetMapping("/healthz")
    @Operation(summary = "Health check", description = "Simple health check endpoint returning status OK.")
    public ResponseEntity<Map<String, Object>> healthz() {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "OK");
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("service", "petclinic");
        return ResponseEntity.ok(body);
    }
}
