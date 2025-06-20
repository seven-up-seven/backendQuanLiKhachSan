package com.example.backendqlks.controller;

import com.example.backendqlks.dto.variable.VariableDto;
import com.example.backendqlks.service.VariableService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Validated
@RestController
@RequestMapping("/api/variable")
public class VariableController {
    private final VariableService variableService;

    public VariableController(VariableService variableService) {
        this.variableService = variableService;
    }

    @GetMapping
    public ResponseEntity<?> getAllVariables() {
        try{
            return ResponseEntity.ok(variableService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching variables: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVariableById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(variableService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching variable with id: " + e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getVariableByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(variableService.getByName(name));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching variable with name: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createVariable(@RequestBody @Valid VariableDto variable) {
        try {
            return ResponseEntity.ok(variableService.create(variable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating variable: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateVariable(@PathVariable int id,
                                            @PathVariable int impactorId,
                                            @PathVariable String impactor,
                                            @RequestBody @Valid VariableDto variable, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.status(500).body(result.getAllErrors());
            }
            return ResponseEntity.ok(variableService.update(id, variable, impactorId, impactor));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating variable: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVariable(@PathVariable int id) {
        try {
            variableService.delete(id);
            return ResponseEntity.ok("Variable deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting variable: " + e.getMessage());
        }
    }
}
