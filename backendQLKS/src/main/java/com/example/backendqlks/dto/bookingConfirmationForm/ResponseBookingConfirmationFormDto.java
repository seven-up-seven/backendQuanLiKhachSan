package com.example.backendqlks.dto.bookingConfirmationForm;

import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.enums.BookingState;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseBookingConfirmationFormDto {
    private int id;
    private Guest bookingGuest;
    private BookingState bookingState;
    private LocalDateTime createdAt;
}
