package com.example.backendqlks.dto.rentalform;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RentalFormDto {
    @NotNull(message = "Room id for rental form can not be null")
    private Integer roomId;

    @NotNull(message = "Staff id for rental form can not be null")
    private Integer staffId;

    @NotNull(message = "Rental date is required for rental form")
    @FutureOrPresent(message = "Rental date can not be in the past")
    private LocalDateTime rentalDate;

    @NotNull(message = "Number of rental day is required for rental form")
    @Min(value = 1, message = "Number of rental day must be at least 1")
    private Short numberOfRentalDays;

    //chi cap nhat sau khi da thanh toan
    private LocalDateTime isPaidAt;

    private String note;
}
