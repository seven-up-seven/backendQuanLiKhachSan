package com.example.backendqlks.dto.rentalextensionform;

import com.example.backendqlks.entity.RentalForm;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRentalExtensionFormDto {

    private RentalForm rentalForm;

    private Short numberOfRentalDays;
}
