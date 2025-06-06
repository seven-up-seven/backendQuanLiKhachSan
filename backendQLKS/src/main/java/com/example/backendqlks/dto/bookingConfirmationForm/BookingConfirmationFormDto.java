package com.example.backendqlks.dto.bookingConfirmationForm;

import com.example.backendqlks.entity.enums.BookingState;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingConfirmationFormDto {
    @NotNull(message = "Booking guest id must not be null or empty")
    @Positive(message = "Booking guest id must be positive")
    private Integer bookingGuestId;

    @NotNull(message = "Booking state must not be null")
    private BookingState bookingState;

    @NotNull(message = "Room id must not be null or empty")
    private Integer roomId;
}
