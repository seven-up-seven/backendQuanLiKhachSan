package com.example.backendqlks.controller;

import com.example.backendqlks.dto.userrole.ResponseUserRoleDto;
import com.example.backendqlks.dto.userrole.UserRoleDto;
import com.example.backendqlks.service.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user-role")
public class UserRoleController {
    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUserRoles() {
        try {
            return ResponseEntity.ok(userRoleService.getAllUserRoles());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user roles: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserRoleById(@PathVariable int id) {
        try {
            ResponseUserRoleDto userRole = userRoleService.getUserRoleById(id);
            return ResponseEntity.ok(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user role with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createUserRole(@PathVariable int impactorId,
                                            @PathVariable String impactor,
                                            @RequestBody @Valid UserRoleDto userRoleDto,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseUserRoleDto createdUserRole = userRoleService.createUserRole(userRoleDto, impactorId, impactor);
            return ResponseEntity.ok(createdUserRole);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating user role: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateUserRole(@PathVariable int id,
                                            @PathVariable int impactorId,
                                            @PathVariable String impactor,
                                            @RequestBody @Valid UserRoleDto userRoleDto,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseUserRoleDto updatedUserRole = userRoleService.updateUserRole(id, userRoleDto, impactorId, impactor);
            return ResponseEntity.ok(updatedUserRole);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user role: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteUserRole(@PathVariable int id,
                                            @PathVariable int impactorId,
                                            @PathVariable String impactor) {
        try {
            userRoleService.deleteUserRoleById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted user role with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user role with id: " + e.getMessage());
        }
    }
}
