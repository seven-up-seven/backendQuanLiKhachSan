package com.example.backendqlks.dto.permission;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionDto {
    @NotBlank(message = "Name must not be null or empty")
    private String name;
}
