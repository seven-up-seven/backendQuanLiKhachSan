package com.example.backendqlks.dto.authentication;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLoginDto {
    private String accessToken;
    private String refreshToken;
    private String message;
}
