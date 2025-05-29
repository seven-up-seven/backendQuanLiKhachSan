package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="SUPPLY")
public class Supply {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="NAME")
    private String name;

    @Column(name="NUMBER")
    private int number;

    @Column(name="PRICE")
    private double price;

    //khoa ngoai toi chi tiet phieu nhap
    @JsonIgnore
    @OneToMany(mappedBy = "supply", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImportInvoiceDetail> importInvoiceDetails;

    //khoa ngoai toi chi tiet phieu trich xuat
    @JsonIgnore
    @OneToMany(mappedBy = "supply", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockRequisitionInvoiceDetail> stockRequisitionInvoiceDetails;

    //khoa ngoai toi RoomSupply
    @JsonIgnore
    @OneToMany(mappedBy = "supply", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomsSupply> roomsSupplies;
}
