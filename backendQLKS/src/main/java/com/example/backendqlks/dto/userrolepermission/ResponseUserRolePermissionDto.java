package com.example.backendqlks.dto.userrolepermission;

import com.example.backendqlks.entity.Permission;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.entity.compositekeys.RolePermissionPrimaryKey;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserRolePermissionDto {
    private RolePermissionPrimaryKey rolePermissionPrimaryKey;

    private int userRoleId;
    private String userRoleName;

    private int permissionId;
    private String permissionName;
}
