package com.example.backendqlks.dto.invoiceDetail;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class InvoiceDetailDto {
    @Positive(message = "Number of rental days must be positive")
    @NotNull(message = "Number of rental days must not be null")
    private Integer numberOfRentalDays; // this column is updated after calculating total rental days of RentalForm and RentalExtensionForm

    @Positive(message = "Invoice id must be positive")
    @NotNull(message = "Invoice id must not be null")
    private Integer invoiceId;

    @PositiveOrZero(message = "Paying guest id must be equals or greater than zero")
    @NotNull(message = "Paying guest id must not be null")
    private Double reservationCost;

    @Positive(message = "Rental form id must be positive")
    @NotNull(message = "Rental form id must not be null")
    private Integer rentalFormId;
}
