package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.permission.ResponsePermissionDto;
import com.example.backendqlks.dto.position.PositionDto;
import com.example.backendqlks.dto.position.ResponsePositionDto;
import com.example.backendqlks.entity.Position;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.entity.UserRolePermission;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    Position toEntity(PositionDto positionDto);
    PositionDto toDto(Position position);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PositionDto positionDto, @MappingTarget Position position);
    @Mapping(target = "staffIds", source = "staffs", qualifiedByName = "staffsToStaffIds")
    @Mapping(target = "staffNames", source = "staffs", qualifiedByName = "staffsToStaffNames")
    ResponsePositionDto toResponseDto(Position position);
    @Mapping(target = "staffIds", source = "staffs", qualifiedByName = "staffsToStaffIds")
    @Mapping(target = "staffNames", source = "staffs", qualifiedByName = "staffsToStaffNames")
    List<ResponsePositionDto> toResponseDtoList(List<Position> positions);

    @Named(value = "staffsToStaffIds")
    default List<Integer> staffsToStaffIds(List<Staff> staffs){
        if(staffs == null) return new ArrayList<>();
        return staffs.stream()
                .filter(Objects::nonNull)
                .map(Staff::getId)
                .toList();
    }

    @Named(value = "staffsToStaffNames")
    default List<String> staffsToStaffNames(List<Staff> staffs){
        if(staffs == null) return new ArrayList<>();
        return staffs.stream()
                .filter(Objects::nonNull)
                .map(Staff::getFullName)
                .toList();
    }
}
