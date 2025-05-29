package com.example.backendqlks.entity;

import jakarta.persistence.*;
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

    @Column(name = "IDENTIFICATION_NUMBER")
    private String identificationNumber;

    @Column(name = "BOOKING_PERSON_AGE")
    private byte bookingPersonAge;
}
