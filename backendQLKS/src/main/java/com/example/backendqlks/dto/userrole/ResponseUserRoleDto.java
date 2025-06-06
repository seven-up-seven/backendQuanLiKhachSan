package com.example.backendqlks.dto.userrole;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseUserRoleDto {
    private Integer id;

    private String name;

    private List<Integer> accountIds;
    private List<String> accountUsernames;

    private List<Integer> permissionIds;
    private List<String> permissionNames;
}
