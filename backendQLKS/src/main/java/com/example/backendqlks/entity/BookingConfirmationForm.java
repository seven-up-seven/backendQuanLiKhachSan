package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.BookingState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKING_CONFIRMATION_FORM")
@Data
public class BookingConfirmationForm {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "BOOKING_GUEST_ID")
    private int bookingGuestId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_GUEST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Guest bookingGuest;

    @Column(name = "BOOKING_STATE")
    @Enumerated(EnumType.STRING)
    private BookingState bookingState;

    @Column(name = "ROOM_ID")
    private int roomId;

    @Column(name = "BOOKING_DATE")
    private LocalDateTime bookingDate;

    @Column(name = "RENTAL_DAYS")
    private int rentalDays;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Room room;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
