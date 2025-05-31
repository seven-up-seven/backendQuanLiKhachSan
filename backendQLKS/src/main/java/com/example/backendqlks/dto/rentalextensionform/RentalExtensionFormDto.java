package com.example.backendqlks.dto.rentalextensionform;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentalExtensionFormDto {

    @NotNull(message = "Number of rental days is required")
    @Min(value = 1, message = "Number of rental days must be at least 1")
    private Short numberOfRentalDays;
}
