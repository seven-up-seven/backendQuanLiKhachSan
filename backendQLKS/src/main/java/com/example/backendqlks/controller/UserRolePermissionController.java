package com.example.backendqlks.controller;

import com.example.backendqlks.service.UserRolePermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-role-permission")
public class UserRolePermissionController {
    private final UserRolePermissionService userRolePermissionService;

    public UserRolePermissionController(UserRolePermissionService userRolePermissionService){
        this.userRolePermissionService = userRolePermissionService;
    }

    //Tạo để lỡ sau này có dùng tới, hiện tại gần như không cần (có lẽ sau này cũng vậy)
}
