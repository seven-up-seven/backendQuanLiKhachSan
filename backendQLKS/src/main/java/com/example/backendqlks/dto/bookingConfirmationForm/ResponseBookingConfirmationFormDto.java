package com.example.backendqlks.dto.bookingConfirmationForm;

import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.enums.BookingState;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class ResponseBookingConfirmationFormDto {
    private int id;
    private Guest bookingGuest;
    private BookingState bookingState;
    private LocalDateTime createdAt;
}
