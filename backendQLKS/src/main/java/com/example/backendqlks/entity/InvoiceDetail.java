package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "INVOICE_DETAIL")
@Data
public class InvoiceDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NUMBER_OF_RENTAL_DAYS")
    private int numberOfRentalDays; // this column is updated after calculating total rental days of RentalForm and RentalExtensionForm

    @Column(name = "INVOICE_ID")
    private int invoiceId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Invoice invoice;

    @Column(name = "RESERVATION_COST")
    private double reservationCost;

    @Column(name = "RENTAL_FORM_ID")
    private int rentalFormId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RENTAL_FORM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private RentalForm rentalForm;
}
