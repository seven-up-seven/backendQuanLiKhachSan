package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="FACILITY")
public class Facility {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="NAME")
    private String name;

    @Column(name="QUANTITY")
    private short quantity;

    @Column(name="PRICE")
    private double price;

    //khoa ngoai toi chi tiet phieu nhap
    @JsonIgnore
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ImportInvoiceDetail> importInvoiceDetails;

    //khoa ngoai toi chi tiet phieu trich xuat
    @JsonIgnore
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StockRequisitionInvoiceDetail> stockRequisitionInvoiceDetails;

    //khoa ngoai toi RoomSupply
    @JsonIgnore
    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomFacility> roomFacilities;
}
