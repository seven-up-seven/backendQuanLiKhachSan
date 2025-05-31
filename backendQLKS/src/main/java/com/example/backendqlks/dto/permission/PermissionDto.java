package com.example.backendqlks.dto.permission;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class PermissionDto {
    @NotBlank(message = "Name must not be null or empty")
    private String name;
}
