package com.example.backendqlks.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class InvoiceDto {
    @PositiveOrZero
    @NotBlank
    private double totalReservationCost;

    @Positive
    @NotBlank
    private int payingGuestId;

    @Positive
    @NotBlank
    private int staffId;
}
