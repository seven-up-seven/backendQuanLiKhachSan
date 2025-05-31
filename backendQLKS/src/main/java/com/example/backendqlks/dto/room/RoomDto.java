package com.example.backendqlks.dto.room;

import com.example.backendqlks.entity.enums.RoomState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomDto {
    @NotBlank(message = "Room name is required")
    private String name;

    private String note;

    @NotNull(message = "Room state is required")
    private RoomState roomState;

    @NotNull(message = "Room type ID is required")
    private Integer roomTypeId;

    @NotNull(message = "Floor ID is required")
    private Integer floorId;
}
