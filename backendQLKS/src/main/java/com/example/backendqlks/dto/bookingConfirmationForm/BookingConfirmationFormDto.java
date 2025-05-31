package com.example.backendqlks.dto.bookingConfirmationForm;

import com.example.backendqlks.entity.enums.BookingState;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingConfirmationFormDto {
    @NotBlank(message = "Booking person name must not be null or empty")
    private String bookingPersonName;

    @NotNull(message = "Booking guest id must not be null or empty")
    @Positive(message = "Booking guest id must be positive")
    private Integer bookingGuestId;

    @NotNull(message = "Booking state must not be null")
    private BookingState bookingState;
}
