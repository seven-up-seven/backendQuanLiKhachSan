package com.example.backendqlks.dto.block;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockDto {
    @NotBlank
    private String name;
}
