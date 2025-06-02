package com.example.backendqlks.controller;

import com.example.backendqlks.dto.permission.PermissionDto;
import com.example.backendqlks.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getPermission(@PathVariable int id){
        try{
            return ResponseEntity.ok(permissionService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching permission: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPermissions(){
        try{
            return ResponseEntity.ok(permissionService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching permissions: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createPermission(@Valid @RequestBody PermissionDto permissionDto,
                                              BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdPermission = permissionService.create(permissionDto);
            return ResponseEntity.ok(createdPermission);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating permission: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable int id,
                                              @Valid @RequestBody PermissionDto permissionDto,
                                              BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedPermission = permissionService.update(id, permissionDto);
            return ResponseEntity.ok(updatedPermission);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating permission: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable int id){
        try{
            permissionService.delete(id);
            return ResponseEntity.ok("Permission deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting permission: " + e.getMessage());
        }
    }
}