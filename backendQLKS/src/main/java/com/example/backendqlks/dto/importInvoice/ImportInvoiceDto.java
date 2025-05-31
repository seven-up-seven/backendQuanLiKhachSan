package com.example.backendqlks.dto.importInvoice;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ImportInvoiceDto {
    @Min(value = 1, message = "Quantity must be a positive number")
    @NotBlank
    private short totalQuantity;

    @PositiveOrZero
    @NotBlank
    private double totalCost;
}
