package com.example.backendqlks.dto.facilityCompensation;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class FacilityCompensationDto {
    @Positive
    @NotBlank
    private int facilityId;

    @Positive
    @NotBlank
    private int invoiceDetailId;

    @Positive
    @NotBlank
    private short quantity;
}
