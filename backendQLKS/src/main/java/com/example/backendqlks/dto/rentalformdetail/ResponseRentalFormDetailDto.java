package com.example.backendqlks.dto.rentalformdetail;

import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.RentalForm;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseRentalFormDetailDto {
    private Integer id;

    private RentalForm rentalForm;

    private Guest guest;
}
