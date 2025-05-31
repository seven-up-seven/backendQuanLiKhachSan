package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "INVOICE")
@Data
public class Invoice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "TOTAL_RESERVATION_COST")
    private double totalReservationCost;

    @Column(name = "PAYING_GUEST_ID")
    private int payingGuestId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYING_GUEST_ID", referencedColumnName = "ID")
    private Guest payingGuest;

    @Column(name = "STAFF_ID")
    private int staffId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Staff staff;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
