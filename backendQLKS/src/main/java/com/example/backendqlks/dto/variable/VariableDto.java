package com.example.backendqlks.dto.variable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariableDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    private double value;
    private String description;
}
