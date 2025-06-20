package com.example.backendqlks.controller;

import com.example.backendqlks.dto.permission.PermissionDto;
import com.example.backendqlks.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/permission")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPermission(@PathVariable int id){
        try{
            return ResponseEntity.ok(permissionService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching permission: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllPermissions(){
        try{
            return ResponseEntity.ok(permissionService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching permissions: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createPermission(@PathVariable int impactorId,
                                              @PathVariable String impactor,
                                              @Valid @RequestBody PermissionDto permissionDto,
                                              BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdPermission = permissionService.create(permissionDto, impactorId, impactor);
            return ResponseEntity.ok(createdPermission);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating permission: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updatePermission(@PathVariable int id,
                                              @PathVariable int impactorId,
                                              @PathVariable String impactor,
                                              @Valid @RequestBody PermissionDto permissionDto,
                                              BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedPermission = permissionService.update(id, permissionDto, impactorId, impactor);
            return ResponseEntity.ok(updatedPermission);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating permission: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deletePermission(@PathVariable int id,
                                              @PathVariable int impactorId,
                                              @PathVariable String impactor) {
        try {
            permissionService.delete(id, impactorId, impactor);
            return ResponseEntity.ok("Permission deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting permission: " + e.getMessage());
        }
    }
}