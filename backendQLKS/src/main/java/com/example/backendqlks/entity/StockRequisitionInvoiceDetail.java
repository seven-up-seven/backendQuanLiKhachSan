package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "STOCK_REQUISITION_DETAIL")
public class StockRequisitionInvoiceDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "TOTAL_QUANTITY")
    private short totalQuantity;

    //khoa ngoai toi vat tu
    @Column(name = "FACILITY_ID")
    private int facilityId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private Facility facility;

    //khoa ngoai toi phieu trich xuat
    @Column(name = "STOCK_REQUISITION_INVOICE_ID")
    private int stockRequisitionInvoiceId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_REQUISITION_INVOICE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private StockRequisitionInvoice stockRequisitionInvoice;
}
