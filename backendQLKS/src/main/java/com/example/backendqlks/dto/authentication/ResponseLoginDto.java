package com.example.backendqlks.dto.authentication;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class ResponseLoginDto {
    private String token;
    private String message;
    public ResponseLoginDto(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
