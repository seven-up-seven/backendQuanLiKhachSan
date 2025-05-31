package com.example.backendqlks.dto.permission;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class PermissionDto {
    @NotBlank
    private String name;
}
