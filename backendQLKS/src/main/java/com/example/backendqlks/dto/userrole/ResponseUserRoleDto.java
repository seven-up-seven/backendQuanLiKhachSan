package com.example.backendqlks.dto.userrole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserRoleDto {
    private Integer id;

    private String name;
}
