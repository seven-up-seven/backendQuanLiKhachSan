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
    private BookingState bookingState;
    private LocalDateTime createdAt;
    private LocalDateTime bookingDate;
    private int rentalDays;
    //guest
    private String guestName;
    private String guestEmail;
    private String guestPhoneNumber;
    private int guestId;
    private String guestIdentificationNumber;
    //room
    private int roomId;
    private String roomName;
    private String roomTypeName;
}
