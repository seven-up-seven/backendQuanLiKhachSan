package com.example.backendqlks.dto.position;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PositionDto {
    @NotBlank
    private String name;

    @Positive
    @NotBlank
    private double baseSalary;
}
