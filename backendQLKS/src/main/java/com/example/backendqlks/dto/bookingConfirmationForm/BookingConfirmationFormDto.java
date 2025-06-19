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

    @NotNull(message = "Booking date must not be null or empty")
//    @FutureOrPresent(message = "Booking date must be in the future or present")
    private LocalDate bookingDate;

    @NotNull(message = "Rental days must not be null or empty")
    @Positive(message = "Rental days must be positive")
    private Integer rentalDays;
}
