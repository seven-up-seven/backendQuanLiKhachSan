package com.example.backendqlks.dto.roomtype;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomTypeDto {
    @NotBlank(message = "Room type name is required")
    private String name;

    @NotNull(message = "Room price is required")
    @Min(value = 0, message = "Room price must be non-negative")
    private Double price;
}
