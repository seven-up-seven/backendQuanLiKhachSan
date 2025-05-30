package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.BookingState;
import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.Data;

@Entity
@Table(name = "BOOKING_CONFIRMATION_FORM")
@Data
public class BookingConfirmationForm {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "BOOKING_PERSON_NAME")
    private String bookingPersonName;

    @Column(name = "BOOKING_PERSON_IDENTIFICATION_NUMBER")
    private String bookingPersonIdentificationNumber;

    @Column(name = "BOOKING_PERSON_PHONE_NUMBER")
    private String bookingPersonPhoneNumber;

    @Column(name = "BOOKING_PERSON_EMAIL")
    private String bookingPersonEmail;

    @Column(name = "BOOKING_PERSON_AGE")
    private byte bookingPersonAge;

    @Column(name = "BOOKING_STATE")
    @Enumerated(EnumType.STRING)
    private BookingState bookingState;
}
