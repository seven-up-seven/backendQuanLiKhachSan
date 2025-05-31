package com.example.backendqlks.dto.facility;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacilityDto {
    @NotBlank
    @Column(name="NAME")
    private String name;

    @Positive
    @Column(name="QUANTITY")
    @NotBlank
    private short quantity;

    @PositiveOrZero
    @Column(name="PRICE")
    @NotBlank
    private double price;
}
