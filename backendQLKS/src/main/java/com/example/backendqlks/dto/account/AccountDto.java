package com.example.backendqlks.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    @NotBlank
    private String userName;

    @NotBlank
    private String passWord;

    @Positive
    @NotBlank
    private int userRoleId;
}
