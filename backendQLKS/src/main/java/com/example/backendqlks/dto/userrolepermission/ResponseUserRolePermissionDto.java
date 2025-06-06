package com.example.backendqlks.dto.userrolepermission;

import com.example.backendqlks.entity.Permission;
import com.example.backendqlks.entity.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserRolePermissionDto {
    private int userRoleId;
    private String userRoleName;

    private int permissionId;
    private String permissionName;
}
