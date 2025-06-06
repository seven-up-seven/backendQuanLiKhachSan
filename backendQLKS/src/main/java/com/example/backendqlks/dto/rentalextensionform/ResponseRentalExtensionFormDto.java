package com.example.backendqlks.dto.rentalextensionform;

import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.Staff;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRentalExtensionFormDto {

    private int id;

    private int rentalFormId;

    private String rentalFormRoomName;

    private Short numberOfRentalDays;

    private int staffId;
    private String staffName;
}
