package com.example.backendqlks.dto.guest;

import com.example.backendqlks.entity.enums.Sex;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuestDto {
    @NotBlank(message = "Name must not be null or empty")
    private String name;

    @NotNull(message = "Sex must not be null")
    private Sex sex;

    @Positive(message = "Age must be positive")
    @NotNull(message = "Age must not be null")
    private Short age;

    @Pattern(regexp = "^\\d{12}$", message = "Identification number must be a 12-digit number")
    private String identificationNumber;

    @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must be 10 or 11 digits")
    private String phoneNumber;

    @Email(message = "Must be email type")
    private String email;

    @Positive(message = "Account id must be positive")
    private Integer accountId;
}
