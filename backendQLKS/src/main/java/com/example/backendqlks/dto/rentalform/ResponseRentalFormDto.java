package com.example.backendqlks.dto.rentalform;

import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.Staff;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseRentalFormDto {
    private Integer id;

    private Room room;

    private Staff staff;

    private LocalDateTime rentalDate;

    private Short numberOfRentalDays;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime isPaidAt;
}
