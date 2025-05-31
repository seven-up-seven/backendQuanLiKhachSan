package com.example.backendqlks.dto.bookingConfirmationForm;

import com.example.backendqlks.entity.enums.BookingState;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class ResponseBookingConfirmationFormDto {
    private int id;
    private String bookingPersonName;
    private String bookingPersonIdentificationNumber;
    private String bookingPersonPhoneNumber;
    private String bookingPersonEmail;
    private byte bookingPersonAge;
    private BookingState bookingState;
}
