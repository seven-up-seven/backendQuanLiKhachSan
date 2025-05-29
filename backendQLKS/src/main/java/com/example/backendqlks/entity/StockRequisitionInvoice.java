package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="STOCK_REQUISITION_INVOICE")
public class StockRequisitionInvoice {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="NUMBER")
    private int number;

    //khoa ngoai toi chi tiet phieu trich xuat
    @JsonIgnore
    @OneToMany(mappedBy = "stockRequisitionInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StockRequisitionInvoiceDetail> stockRequisitionInvoiceDetails;

    //khoa ngoai toi phong
    @Column(name = "ROOM_ID")
    private String roomId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Room room;
}
