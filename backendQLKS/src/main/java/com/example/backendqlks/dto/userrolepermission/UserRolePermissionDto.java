package com.example.backendqlks.dto.userrolepermission;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRolePermissionDto {
    @NotNull(message = "User role ID is required")
    private Integer userRoleId;

    @NotNull(message = "Permission ID is required")
    private Integer permissionId;
}
