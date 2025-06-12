package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.userrolepermission.UserRolePermissionDto;
import com.example.backendqlks.dto.variable.ResponseVariableDto;
import com.example.backendqlks.dto.variable.VariableDto;
import com.example.backendqlks.entity.UserRolePermission;
import com.example.backendqlks.entity.Variable;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VariableMapper {
    VariableDto toDto(Variable variable);
    Variable toEntity(VariableDto variableDto);

    void updateEntityFromDto(VariableDto variableDto, Variable variable);
    ResponseVariableDto toResponseDto(Variable variable);
    List<ResponseVariableDto> toResponseDtoList(List<Variable> variables);
}
