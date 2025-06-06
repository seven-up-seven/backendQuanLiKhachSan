package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.userrolepermission.ResponseUserRolePermissionDto;
import com.example.backendqlks.dto.userrolepermission.UserRolePermissionDto;
import com.example.backendqlks.entity.Permission;
import com.example.backendqlks.entity.UserRolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRolePermissionMapper {
    UserRolePermissionDto toDto(UserRolePermission userRolePermission);
    UserRolePermission toEntity(UserRolePermissionDto userRolePermissionDto);

    void updateEntityFromDto(UserRolePermissionDto userRolePermissionDto, @MappingTarget UserRolePermission userRolePermission);

    @Mapping(target = "userRoleId", source = "userRole.id")
    @Mapping(target = "userRoleName", source = "userRole.name")
    @Mapping(target = "permissionId", source = "permission.id")
    @Mapping(target = "permissionName", source = "permission.name")
    ResponseUserRolePermissionDto toResponseDto(UserRolePermission userRolePermission);

    @Mapping(target = "userRoleId", source = "userRole.id")
    @Mapping(target = "userRoleName", source = "userRole.name")
    @Mapping(target = "permissionId", source = "permission.id")
    @Mapping(target = "permissionName", source = "permission.name")
    List<ResponseUserRolePermissionDto> toResponseDtoList(List<UserRolePermission> userRolePermissions);
}
