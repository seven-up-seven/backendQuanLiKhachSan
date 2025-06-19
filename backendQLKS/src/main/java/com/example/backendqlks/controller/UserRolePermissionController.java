package com.example.backendqlks.controller;

import com.example.backendqlks.dto.userrolepermission.UserRolePermissionDto;
import com.example.backendqlks.mapper.UserRolePermissionMapper;
import com.example.backendqlks.service.UserRolePermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user-role-permission")
public class UserRolePermissionController {
    private final UserRolePermissionService userRolePermissionService;

    public UserRolePermissionController(UserRolePermissionService userRolePermissionService) {
        this.userRolePermissionService = userRolePermissionService;
    }

    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createUserRolePermission(@RequestBody @Valid  UserRolePermissionDto userRolePermissionDto, BindingResult result,
                                                      @PathVariable int impactorId,
                                                      @PathVariable String impactor) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdUserRolePermission=userRolePermissionService.createUserRolePermission(userRolePermissionDto, impactorId, impactor);
            return ResponseEntity.ok(createdUserRolePermission);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating user role permission: "+e.getMessage());
        }
    }

//    @PutMapping("/{impactorId}/{impactor}")
//    public ResponseEntity<?> updateUserRolePermission(@RequestBody @Valid UserRolePermissionDto userRolePermissionDto, BindingResult result,
//                                                      @PathVariable int impactorId, @PathVariable String impactor) {
//        try {
//            if (result.hasErrors()) {
//                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
//            }
//            var updateObject=userRolePermissionService.updateUserRolePermission(userRolePermissionDto);
//        }
//    }
}
