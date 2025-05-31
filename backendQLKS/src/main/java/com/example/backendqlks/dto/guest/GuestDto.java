package com.example.backendqlks.dto.guest;

import com.example.backendqlks.entity.enums.Sex;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public class GuestDto {
    @NotBlank
    private String name;

    @NotNull
    private Sex sex;

    @Positive
    private short  age;

    //TODO: add another option for foreigners
    @Pattern(regexp = "^[1-9]\\d{11}$", message = "Identification number must be a 12-digit number")
    private String identificationNumber;

    private String address;

    @Pattern(regexp = "^\\d+$\n")
    private String phoneNumber;

    @Email
    private String email;

    //TODO: change from string to Enum or Entity Type
    @NotBlank
    private  String nationality;
}
