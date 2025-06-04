package com.example.backendqlks.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

public record LoginDto (
        String username,
        String password
) {}
