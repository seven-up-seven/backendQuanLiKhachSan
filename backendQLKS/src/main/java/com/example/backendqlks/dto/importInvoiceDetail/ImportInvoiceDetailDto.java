package com.example.backendqlks.dto.importInvoiceDetail;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ImportInvoiceDetailDto {
    @Min(value = 1, message = "Quantity must be positive")
    @NotBlank
    private short quantity;

    @PositiveOrZero
    @NotBlank
    private double cost;

    @Positive
    @NotBlank
    private int facilityId;

   @Positive
    @NotBlank
    private int importInvoiceId;
}
