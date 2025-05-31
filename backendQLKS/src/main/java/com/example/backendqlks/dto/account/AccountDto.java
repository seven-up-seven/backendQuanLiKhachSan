package com.example.backendqlks.dto.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    @
    private String userName;
    private String passWord;
    private int userRoleId;
}
