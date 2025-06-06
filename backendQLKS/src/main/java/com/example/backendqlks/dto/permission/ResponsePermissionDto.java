package com.example.backendqlks.dto.permission;

import com.example.backendqlks.entity.UserRolePermission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponsePermissionDto {
    private int id;
    private String name;
    private List<Integer> userRoleIds; // userRolePermission contains a composite key
    private List<String> userRoleNames;
}
