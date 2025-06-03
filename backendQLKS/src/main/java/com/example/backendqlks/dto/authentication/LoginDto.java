package com.example.backendqlks.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    @NotBlank(message = "Username can't be null or empty")
    private String username;
    @NotBlank(message = "Password can't be null or empty")
    private String password;
}
