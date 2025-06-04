package com.example.backendqlks.dto.authentication;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public record ResponseLoginDto (
        String accessToken,
        String refreshToken,
        //Don't know why I added message to the response, I'm getting crazy
        String message
) {}
