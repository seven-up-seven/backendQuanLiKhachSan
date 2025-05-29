package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REVENUE_REPORT")
@Data
public class RevenueReport {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "YEAR")
    private byte year;

    @Column(name = "MONTH")
    private byte month;

    @Column(name = "TOTAL_MONTH_REVENUE")
    private double totalMonthRevenue;

    @JsonIgnore
    @OneToMany(mappedBy = "revenueReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RevenueReportDetail> revenueReportDetailList = new ArrayList<>();
}
