package com.example.backendqlks.dto.block;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockDto {
    @NotBlank(message = "Block name must not be null or empty")
    private String name;

    @Nullable
    private Double posX;

    @Nullable
    private Double posY;
}
