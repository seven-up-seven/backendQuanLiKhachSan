package com.example.backendqlks.dto.account;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseAccountDto {
    private int id;
    private String username;
    private String password;
    //userRole
    private int userRoleId;
    private String userRoleName;

    private List<Integer> userRolePermissionIds;

    private List<String> userRolePermissionNames;
}
