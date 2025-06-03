package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.userrole.ResponseUserRoleDto;
import com.example.backendqlks.dto.userrole.UserRoleDto;
import com.example.backendqlks.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {
    UserRole toEntity(UserRoleDto userRoleDto);
    UserRoleDto toDto(UserRole userRole);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserRoleDto userRoleDto, @MappingTarget UserRole userRole);

    ResponseUserRoleDto toResponseDto(UserRole userRole);
    List<ResponseUserRoleDto> toResponseDtoList(List<UserRole> userRoles);
}
