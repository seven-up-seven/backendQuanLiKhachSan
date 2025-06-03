package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RENTAL_FORM")
@Data
public class RentalForm {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ROOM_ID")
    private int roomId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID")
    private Room room;

    @Column(name = "STAFF_ID")
    private int staffId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Staff staff;

    @Column(name = "RENTAL_DATE")
    private LocalDateTime rentalDate;

    @Column(name = "IS_PAID_AT")
    @Nullable
    private LocalDateTime isPaidAt;

    @Column(name = "NUMBER_OF_RENTAL_DAY")
    private short numberOfRentalDays;

    @Column(name = "NOTE")
    @Nullable
    private String note;

    //khoa ngoai toi InvoiceDetail
    @JsonIgnore
    @OneToOne(mappedBy = "rentalForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private InvoiceDetail invoiceDetail;

    //khoa ngoai toi RentalFormDetail
    @JsonIgnore
    @OneToMany(mappedBy = "rentalForm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RentalFormDetail> rentalFormDetails = new ArrayList<>();

    //khoa ngoai toi RentalExtensionForm
    @JsonIgnore
    @OneToMany(mappedBy = "rentalForm", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RentalExtensionForm> rentalExtensionForms = new ArrayList<>();

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
