package com.example.backendqlks.controller;

import com.example.backendqlks.dto.userrole.ResponseUserRoleDto;
import com.example.backendqlks.dto.userrole.UserRoleDto;
import com.example.backendqlks.service.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<?> getAllUserRoles() {
        try {
            return ResponseEntity.ok(userRoleService.getAllUserRoles());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user roles: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserRoleById(@PathVariable int id) {
        try {
            ResponseUserRoleDto userRole = userRoleService.getUserRoleById(id);
            return ResponseEntity.ok(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user role with id: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createUserRole(@RequestBody @Valid UserRoleDto userRoleDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseUserRoleDto createdUserRole = userRoleService.createUserRole(userRoleDto);
            return ResponseEntity.ok(createdUserRole);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating user role: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserRole(@PathVariable int id, @RequestBody @Valid UserRoleDto userRoleDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseUserRoleDto updatedUserRole = userRoleService.updateUserRole(id, userRoleDto);
            return ResponseEntity.ok(updatedUserRole);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user role: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserRole(@PathVariable int id) {
        try {
            userRoleService.deleteUserRoleById(id);
            return ResponseEntity.ok("Deleted user role with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user role with id: " + e.getMessage());
        }
    }
}
