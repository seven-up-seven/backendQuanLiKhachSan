package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

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

    //TODO: add 2 entity with many to one relationships

    @Column(name = "QUANTITY")
    private int quantity;
}
