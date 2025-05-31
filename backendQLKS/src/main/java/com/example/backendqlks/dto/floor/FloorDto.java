package com.example.backendqlks.dto.floor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class FloorDto {
    @NotBlank
    private String name;

    @Positive
    @NotBlank
    private int blockId;
}
