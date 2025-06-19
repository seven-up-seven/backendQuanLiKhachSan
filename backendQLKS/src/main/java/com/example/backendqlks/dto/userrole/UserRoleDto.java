package com.example.backendqlks.dto.userrole;

import com.example.backendqlks.dto.userrolepermission.UserRolePermissionDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRoleDto {
    @NotBlank(message = "Role name is required")
    private String name;

    @Nullable
    private List<UserRolePermissionDto> listPermissions;
}
