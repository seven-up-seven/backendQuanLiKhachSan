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

    private List<Integer> permissionIds;
}
