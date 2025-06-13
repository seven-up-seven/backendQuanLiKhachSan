package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.userrolepermission.UserRolePermissionDto;
import com.example.backendqlks.dto.variable.ResponseVariableDto;
import com.example.backendqlks.dto.variable.VariableDto;
import com.example.backendqlks.entity.UserRolePermission;
import com.example.backendqlks.entity.Variable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = VariableMapper.class)
public interface VariableMapper {
    VariableDto toDto(Variable variable);

    Variable toEntity(VariableDto variableDto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(VariableDto variableDto, @MappingTarget Variable variable);
    
    ResponseVariableDto toResponseDto(Variable variable);

    List<ResponseVariableDto> toResponseDtoList(List<Variable> variables);
}
