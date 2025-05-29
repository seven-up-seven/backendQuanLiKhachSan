package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "IMPORT_INVOICE_DETAIL")
public class ImportInvoiceDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NUMBER")
    private int number;

    @Column(name = "COST")
    private double cost;

    @Column(name = "SUPPLY_ID")
    private int supplyId;

    @Column(name = "IMPORT_INVOICE_ID")
    private int importInvoiceId;

    //khoa ngoai toi vat tu (Supply)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPPLY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Supply supply;

    //khoa ngoai toi phieu nhap
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPORT_INVOICE_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private ImportInvoice importInvoice;
}
