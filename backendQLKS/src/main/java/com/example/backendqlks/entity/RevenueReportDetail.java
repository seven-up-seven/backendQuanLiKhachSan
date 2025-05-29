package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "REVENUE_REPORT_DETAIL")
@Data
public class RevenueReportDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "TOTAL_ROOM_REVENUE")
    private double totalRoomRevenue;

    @Column(name = "REVENUE_REPORT_ID")
    private int revenueReportId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REVENUE_REPORT_ID", referencedColumnName = "ID")
    private RevenueReport revenueReport;

    @Column(name = "ROOMTYPE_ID")
    private int roomTypeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOMTYPE_ID", referencedColumnName = "ID")
    private RoomType roomType;
}
