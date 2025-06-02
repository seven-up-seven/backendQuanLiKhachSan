package com.example.backendqlks.dto.account;

import com.example.backendqlks.entity.UserRole;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseAccountDto {
    private int id;
    private String username;
    private String password;
    private UserRole userRole;
}
