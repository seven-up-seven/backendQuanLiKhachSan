package com.example.backendqlks.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class InvoiceDto {
    @PositiveOrZero(message = "Total reservation cost must be equals or greater than zero")
    @NotNull(message = "Total reservation cost must not be null")
    private Double totalReservationCost;

    @Positive(message = "Paying guest id must be positive")
    @NotNull(message = "Paying guest id must not be null")
    private Integer payingGuestId;

    @Positive(message = "Staff id must be positive")
    @NotNull(message = "Staff id must not be null")
    private Integer staffId;
}
