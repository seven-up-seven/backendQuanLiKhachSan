package com.example.backendqlks.dto.rentalformdetail;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentalFormDetailDto {
    @NotNull(message = "Rental Form ID is required")
    private Integer rentalFormId;

    @NotNull(message = "Guest ID is required")
    private Integer guestId;
}
