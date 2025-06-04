package com.example.backendqlks.dto.room;

import com.example.backendqlks.dto.floor.FloorDto;
import com.example.backendqlks.entity.Floor;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.entity.enums.RoomState;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseRoomDto {
    private Integer id;

    private String name;

    private String note;

    private RoomState roomState;

    private RoomType roomType;

    private Floor floor;

    private List<Integer> bookingConfirmationFormIds;

    private List<Integer> rentalFormIds;
}
