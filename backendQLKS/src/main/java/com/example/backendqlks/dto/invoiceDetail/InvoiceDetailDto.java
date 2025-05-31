package com.example.backendqlks.dto.invoiceDetail;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class InvoiceDetailDto {
   @Positive
   @NotBlank
    private int numberOfRentalDays; // this column is updated after calculating total rental days of RentalForm and RentalExtensionForm

    @Positive
    @NotBlank
    private int invoiceId;

    @PositiveOrZero
    @NotBlank
    private double reservationCost;

    @Positive
    @NotBlank
    private int rentalFormId;
}
