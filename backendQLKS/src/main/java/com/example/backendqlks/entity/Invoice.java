package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "INVOICE")
@Data
public class Invoice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "TOTAL_INVOICES_VALUE")
    private double totalInvoicesValue;

    @Column(name = "PAYING_GUEST_ID")
    private int payingGuestId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYING_GUEST_ID", referencedColumnName = "ID")
    private Guest payingGuest;

    @Column(name = "STAFF_ID")
    private int staffId;

//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "STAFF_ID", referencedColumnName = "ID")
    //private Staff staff;
    //TODO: add OneToMany relationship in Staff
}
