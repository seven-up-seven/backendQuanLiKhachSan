package com.example.backendqlks.dto.revenuereportdetail;

import com.example.backendqlks.entity.RevenueReport;
import com.example.backendqlks.entity.RoomType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRevenueReportDetailDto {
    private Integer id;

    private Double totalRoomRevenue;

    private int revenueReportId;

    private int roomTypeId;
    private String roomTypeName;
}
