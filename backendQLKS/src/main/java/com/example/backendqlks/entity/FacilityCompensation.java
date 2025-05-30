package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "FACILITY_COMPENSATION")
@Data
@IdClass(FacilityCompensation.class)
public class FacilityCompensation {
    @Id
    @Column(name = "FACILITY_ID")
    private int facilityId;

    @Id
    @Column(name = "INVOICE_DETAIL_ID")
    private int invoiceDetailId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Facility facility;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_DETAIL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private InvoiceDetail invoiceDetail;

    @Column(name = "QUANTITY")
    private short quantity;
}
