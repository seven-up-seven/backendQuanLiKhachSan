package com.example.backendqlks.dto.roomtype;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseRoomTypeDto {
    private Integer id;

    private String name;

    private Double price;

    private List<Integer> roomIds;

    private List<Integer> revenueReportDetailIds;
}
