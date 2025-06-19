package com.example.backendqlks.controller;

import com.example.backendqlks.dto.position.PositionDto;
import com.example.backendqlks.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/position")
public class PositionController {
    private final PositionService positionService;

    public PositionController(PositionService positionService){
        this.positionService = positionService;
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPosition(@PathVariable int id){
        try{
            return ResponseEntity.ok(positionService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching position: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT')")
    @GetMapping
    public ResponseEntity<?> getAllPositions(){
        try{
            return ResponseEntity.ok(positionService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching positions: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createPosition(@PathVariable int impactorId,
                                            @PathVariable String impactor,
                                            @Valid @RequestBody PositionDto positionDto,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdPosition = positionService.create(positionDto, impactorId, impactor);
            return ResponseEntity.ok(createdPosition);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating position: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updatePosition(@PathVariable int id,
                                            @PathVariable int impactorId,
                                            @PathVariable String impactor,
                                            @Valid @RequestBody PositionDto positionDto,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedPosition = positionService.update(id, positionDto, impactorId, impactor);
            return ResponseEntity.ok(updatedPosition);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating position: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deletePosition(@PathVariable int id,
                                            @PathVariable int impactorId,
                                            @PathVariable String impactor) {
        try {
            positionService.delete(id, impactorId, impactor);
            return ResponseEntity.ok("Position deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting position: " + e.getMessage());
        }
    }
}