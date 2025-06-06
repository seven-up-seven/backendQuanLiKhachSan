package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.permission.PermissionDto;
import com.example.backendqlks.dto.permission.ResponsePermissionDto;
import com.example.backendqlks.entity.Permission;
import com.example.backendqlks.entity.UserRolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity(PermissionDto permissionDto);
    PermissionDto toDto(Permission permission);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PermissionDto permissionDto, @MappingTarget Permission permission);

    @Mapping(target = "userRoleIds", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToUserRoleIds")
    @Mapping(target = "userRoleNames", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToUserRoleNames")
    ResponsePermissionDto toResponseDto(Permission permission);
    @Mapping(target = "userRoleIds", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToUserRoleIds")
    @Mapping(target = "userRoleNames", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToUserRoleNames")
    List<ResponsePermissionDto> toResponseDtoList(List<Permission> permissions);

    @Named(value = "userRolePermissionsToUserRoleIds")
    default List<Integer> userRolePermissionsToUserRoleIds(List<UserRolePermission> userRolePermissions){
        if(userRolePermissions == null) return new ArrayList<>();
        return userRolePermissions.stream()
                .filter(Objects::nonNull)
                .map(UserRolePermission::getUserRoleId)
                .toList();
    }

    @Named(value = "userRolePermissionsToUserRoleNames")
    default List<String> userRolePermissionsToUserRoleNames(List<UserRolePermission> userRolePermissions){
        if(userRolePermissions == null) return new ArrayList<>();
        return userRolePermissions.stream()
                .filter(Objects::nonNull)
                .map(urp -> urp.getUserRole() != null ? urp.getUserRole().getName() : null)
                .filter(Objects::nonNull)
                .toList();
    }
}
