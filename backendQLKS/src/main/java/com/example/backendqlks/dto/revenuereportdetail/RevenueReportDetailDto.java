package com.example.backendqlks.dto.revenuereportdetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RevenueReportDetailDto {
    @NotNull(message = "Total room revenue is required")
    @Min(value = 0, message = "Total room revenue must be non-negative")
    private Double totalRoomRevenue;

    @NotNull(message = "Revenue report ID is required")
    private Integer revenueReportId;

    @NotNull(message = "Room type ID is required")
    private Integer roomTypeId;
}
