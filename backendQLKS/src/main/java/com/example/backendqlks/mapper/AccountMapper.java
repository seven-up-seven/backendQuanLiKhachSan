package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.dto.account.ResponseAccountDto;
import com.example.backendqlks.entity.Account;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.entity.UserRolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserRoleMapper.class)
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);
    AccountDto toDto(Account account);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AccountDto accountDto, @MappingTarget Account account);

    @Mapping(target = "userRoleId", source = "userRole", qualifiedByName = "toId")
    @Mapping(target = "userRoleName", source = "userRole", qualifiedByName = "toName")
    @Mapping(target = "userRolePermissionIds", source = "userRole", qualifiedByName = "toUserRolePermissionIds")
    @Mapping(target = "userRolePermissionNames", source = "userRole", qualifiedByName = "toUserRolePermissionNames")
    ResponseAccountDto toResponseDto(Account account);
    @Mapping(target = "userRoleId", source = "userRole", qualifiedByName = "toId")
    @Mapping(target = "userRoleName", source = "userRole", qualifiedByName = "toName")
    @Mapping(target = "userRolePermissionIds", source = "userRole", qualifiedByName = "toUserRolePermissionIds")
    @Mapping(target = "userRolePermissionNames", source = "userRole", qualifiedByName = "toUserRolePermissionNames")
    List<ResponseAccountDto> toResponseDtoList(List<Account> accounts);

    @Named(value = "toId")
    default Integer toId(UserRole userRole) {
        return userRole != null ? userRole.getId() : null;
    }

    @Named(value = "toName")
    default String toName(UserRole userRole) {
        return userRole != null ? userRole.getName() : null;
    }

    @Named(value = "toUserRolePermissionIds")
    default List<Integer> toUserRolePermissionIds(UserRole userRole) {
        return userRole != null && userRole.getUserRolePermissions() != null
                ? userRole.getUserRolePermissions().stream()
                    .map(UserRolePermission::getPermissionId)
                    .toList()
                : List.of();
    }

    @Named(value = "toUserRolePermissionNames")
    default List<String> toUserRolePermissionNames(UserRole userRole) {
        return userRole != null && userRole.getUserRolePermissions() != null
                ? userRole.getUserRolePermissions().stream()
                    .map(permission -> permission.getPermission().getName())
                    .toList()
                : List.of();
    }
}
