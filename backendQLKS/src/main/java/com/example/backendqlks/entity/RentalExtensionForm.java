package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "RENTAL_EXTENSION_FORM")
@Data
public class RentalExtensionForm {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "RENTAL_FORM_ID")
    private int rentalFormId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RENTAL_FORM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private RentalFormDetail rentalForm;

    @Column(name = "NUMBER_OF_RENTAL_DAY")
    private short numberOfRentalDays;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
