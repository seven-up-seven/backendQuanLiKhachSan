package com.example.backendqlks.dto.facilityCompensation;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FacilityCompensationDto {
    @Positive(message = "guest id must be positive")
    @NotNull(message = "Booking state must not be null")
    private Integer facilityId;

    @Positive(message = "Booking guest id must be positive")
    @NotNull(message = "Booking state must not be null")
    private Integer invoiceDetailId;

    @Positive(message = "Booking guest id must be positive")
    @NotNull(message = "Booking state must not be null")
    private Short quantity;
}
