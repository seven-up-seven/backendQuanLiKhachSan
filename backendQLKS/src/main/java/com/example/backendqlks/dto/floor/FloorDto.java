package com.example.backendqlks.dto.floor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FloorDto {
    @NotBlank(message = "Block id must not be null or empty")
    private String name;

    @Positive(message = "Block id must be positive")
    @NotNull(message = "Block id must not be null")
    private Integer blockId;
}
