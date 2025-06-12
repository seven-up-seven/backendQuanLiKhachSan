package com.example.backendqlks.dto.revenuereport;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevenueReportDto {
    @NotNull(message = "Year is required")
    private Short year;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Byte month;

    private Double totalMonthRevenue;
}



