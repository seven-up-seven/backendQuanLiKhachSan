package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="IMPORT_INVOICE")
public class ImportInvoice {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="TOTAL_QUANTITY")
    private short totalQuantity;

    @Column(name="TOTAL_COST")
    private double totalCost;

    //khoa ngoai toi chi tiet phieu nhap
    @JsonIgnore
    @OneToMany(mappedBy = "importInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ImportInvoiceDetail> importInvoiceDetails = new ArrayList<>();
}
