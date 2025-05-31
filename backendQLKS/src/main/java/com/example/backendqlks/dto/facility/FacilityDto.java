package com.example.backendqlks.dto.facility;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacilityDto {
    @NotBlank
    private String name;

    @Positive(message = "Quantity must be positive")
    @NotNull(message = "Quantity must not be null")
    private Short quantity;

    @PositiveOrZero(message = "Price must be equals or greater than zero")
    @NotNull(message = "Price must not be null")
    private Double price;
}
