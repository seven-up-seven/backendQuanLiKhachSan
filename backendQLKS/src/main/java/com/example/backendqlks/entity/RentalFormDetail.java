package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "RENTAL_FORM_DETAIL")
@Data
public class RentalFormDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "RENTAL_FORM_ID")
    private int rentalFormId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RENTAL_FORM_ID", referencedColumnName = "ID")
    private RentalForm rentalForm;

    @Column(name = "GUEST_ID")
    private int guestId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GUEST_ID", referencedColumnName = "ID")
    private Guest guest;
}
