package com.example.backendqlks.dto.guest;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SearchGuestDto {
    Integer id;
    String name;
    @Pattern(regexp = "^(\\d{10,11})$|^$", message = "Phone number must be 10 or 11 digits")
    String phoneNumber;
    @Pattern(regexp = "^\\d{12}$", message = "Identification number must be a 12-digit number")
    String identificationNumber;
    @Pattern(regexp = "^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$|^$", message = "Must be email type")
    String email;
    Integer accountId;
}
