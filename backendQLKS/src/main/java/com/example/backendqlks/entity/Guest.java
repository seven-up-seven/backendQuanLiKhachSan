package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GUEST")
@Data
public class Guest {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SEX")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "AGE")
    private short  age;

    @Nullable
    @Column(name = "IDENTIFICATION_NUMBER")
    private String identificationNumber;

    @Column(name = "PHONE_NUMBER")
    @Nullable
    private String phoneNumber;

    @Column(name = "EMAIL")
    @Nullable
    private String email;

    @Column(name = "ACCOUNT_ID")
    @Nullable
    private Integer accountId; // using integer instead of int to contains null value - we are making a chaotic design

    @JsonIgnore
    @OneToMany(mappedBy = "payingGuest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Invoice> invoices = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RentalFormDetail> rentalFormDetails = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "bookingGuest", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BookingConfirmationForm> bookingConfirmationForms = new ArrayList<>();
}
