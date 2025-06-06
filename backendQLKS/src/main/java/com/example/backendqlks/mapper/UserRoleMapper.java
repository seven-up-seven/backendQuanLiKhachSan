package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.userrole.ResponseUserRoleDto;
import com.example.backendqlks.dto.userrole.UserRoleDto;
import com.example.backendqlks.entity.Account;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.entity.UserRolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {
    UserRole toEntity(UserRoleDto userRoleDto);
    UserRoleDto toDto(UserRole userRole);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserRoleDto userRoleDto, @MappingTarget UserRole userRole);

    @Mapping(target = "accountIds", source = "accounts", qualifiedByName = "accountsToIds")
    @Mapping(target = "accountUsernames", source = "accounts", qualifiedByName = "accountsToUsernames")
    @Mapping(target = "permissionIds", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToIds")
    @Mapping(target = "permissionNames", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToNames")
    ResponseUserRoleDto toResponseDto(UserRole userRole);
    @Mapping(target = "accountIds", source = "accounts", qualifiedByName = "accountsToIds")
    @Mapping(target = "accountUsernames", source = "accounts", qualifiedByName = "accountsToUsernames")
    @Mapping(target = "permissionIds", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToIds")
    @Mapping(target = "permissionNames", source = "userRolePermissions", qualifiedByName = "userRolePermissionsToNames")
    List<ResponseUserRoleDto> toResponseDtoList(List<UserRole> userRoles);

    @Named(value = "accountsToIds")
    default List<Integer> accountsToIds(List<Account> accounts) {
        if(accounts== null) {
            return new ArrayList<>();
        }
        return accounts.stream()
                .filter(Objects::nonNull)
                .map(Account::getId)
                .toList();
    }

    @Named(value = "accountsToUsernames")
    default List<String> accountsToUsernames(List<Account> accounts) {
        if(accounts == null) return new ArrayList<>();
        return accounts.stream()
                .filter(Objects::nonNull)
                .map(Account::getUsername)
                .toList();
    }

    @Named(value = "userRolePermissionsToIds")
    default List<Integer> userRolePermissionsToIds(List<UserRolePermission> userRolePermissions) {
        if(userRolePermissions == null) {
            return new ArrayList<>();
        }
        return userRolePermissions.stream()
                .filter(Objects::nonNull)
                .map(UserRolePermission::getPermissionId)
                .toList();
    }

    @Named(value = "userRolePermissionsToNames")
    default List<String> userRolePermissionsToNames(List<UserRolePermission> userRolePermissions) {
        if(userRolePermissions == null) return new ArrayList<>();
        return userRolePermissions.stream()
                .filter(Objects::nonNull)
                .map(urp -> urp.getPermission() != null ? urp.getPermission().getName() : null)
                .filter(Objects::nonNull)
                .toList();
    }
}
