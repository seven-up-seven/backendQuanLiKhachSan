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
    @NotBlank(message = "Booking person name must not be empty")
    private String bookingPersonName;

    //TODO: add another option for foreigners
    @Pattern(regexp = "^[1-9]\\d{11}$", message = "Identification number must be a 12-digit number")
    private String bookingPersonIdentificationNumber;

    @NotBlank(message = "Phone number must not be empty")
    private String bookingPersonPhoneNumber;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String bookingPersonEmail;

    @Min(value = 18, message = "Booking person must be at least 18 years old")
    @NotBlank
    private byte bookingPersonAge;

    @NotNull(message = "Booking state must not be null")
    private BookingState bookingState;
}
