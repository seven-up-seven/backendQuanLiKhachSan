package com.example.backendqlks.dto.staff;

import com.example.backendqlks.entity.enums.Sex;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffDto {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Minimum age must be 18")
    private Integer age;

    @NotBlank(message = "Identification number is required")
    private String identificationNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Sex is required")
    private Sex sex;

    @Positive(message = "Salary multiplier must be positive")
    private Float salaryMultiplier;

    @NotNull(message = "Position ID is required")
    private Integer positionId;

    private Integer accountId;
}
