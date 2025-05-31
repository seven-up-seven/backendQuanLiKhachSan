package com.example.backendqlks.dto.roomtype;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRoomTypeDto {
    private Integer id;

    private String name;

    private Double price;
}
