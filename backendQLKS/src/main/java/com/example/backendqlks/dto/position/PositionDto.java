package com.example.backendqlks.dto.position;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionDto {
    @NotBlank(message = "Name must not be null")
    private String name;

    @Positive(message = "Base salary must be positive")
    @NotNull(message = "Base salary must not be null")
    private Double baseSalary;
}
