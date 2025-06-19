package com.example.backendqlks.dto.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceDto {
    @PositiveOrZero(message = "Total reservation cost must be equals or greater than zero")
    @NotNull(message = "Total reservation cost must not be null")
    private Double totalReservationCost;

    @Positive(message = "Paying guest id must be positive")
    @NotNull(message = "Paying guest id must not be null")
    private Integer payingGuestId;

    private Integer staffId;
}
