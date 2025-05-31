package com.example.backendqlks.dto.userrole;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRoleDto {
    @NotBlank(message = "Role name is required")
    private String name;
}
