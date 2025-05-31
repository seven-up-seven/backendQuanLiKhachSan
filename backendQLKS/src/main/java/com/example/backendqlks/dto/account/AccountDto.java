package com.example.backendqlks.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    @NotBlank(message = "User name must not be null or empty")
    private String userName;

    @NotBlank(message = "User name must not be null or empty")
    private String passWord;

    @NotNull(message = "User role id must not be null")
    @Positive(message = "User role id must be positive")
    private Integer userRoleId;
}
