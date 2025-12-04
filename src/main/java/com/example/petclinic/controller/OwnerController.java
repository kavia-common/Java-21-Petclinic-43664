package com.example.petclinic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * PUBLIC_INTERFACE
 * OwnerController exposes demo endpoints for owners in the Petclinic app.
 * These are simple in-memory endpoints to demonstrate Swagger/OpenAPI integration.
 */
@RestController
@RequestMapping("/api/owners")
@Tag(name = "Owners", description = "Endpoints to manage Owners in the Petclinic demo")
public class OwnerController {

    private final Map<Long, Map<String, Object>> owners = new HashMap<>();

    public OwnerController() {
        // seed with a sample owner
        Map<String, Object> owner = new HashMap<>();
        owner.put("id", 1L);
        owner.put("firstName", "George");
        owner.put("lastName", "Franklin");
        owners.put(1L, owner);
    }

    // PUBLIC_INTERFACE
    @GetMapping
    @Operation(summary = "List owners", description = "Returns a list of all owners.")
    public ResponseEntity<List<Map<String, Object>>> listOwners() {
        return ResponseEntity.ok(new ArrayList<>(owners.values()));
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(summary = "Get owner by id", description = "Returns owner details for the given id.")
    public ResponseEntity<Map<String, Object>> getOwner(@PathVariable Long id) {
        Map<String, Object> owner = owners.get(id);
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(owner);
    }
}
