package com.example.backendqlks.dto.revenuereport;

import com.example.backendqlks.entity.RevenueReportDetail;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseRevenueReportDto {
    private Integer id;

    private Short year;

    private Byte month;

    private Double totalMonthRevenue;

    private List<RevenueReportDetail> revenueReportDetails;
}
