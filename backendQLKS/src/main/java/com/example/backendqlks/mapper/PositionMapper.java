package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.permission.ResponsePermissionDto;
import com.example.backendqlks.dto.position.PositionDto;
import com.example.backendqlks.dto.position.ResponsePositionDto;
import com.example.backendqlks.entity.Position;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.entity.UserRolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    Position toEntity(PositionDto positionDto);
    PositionDto toDto(Position position);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PositionDto positionDto, @MappingTarget Position position);

    ResponsePositionDto toResponseDto(Position position);
    List<ResponsePositionDto> toResponseDtoList(List<Position> positions);

    default List<Integer> staffsToStaffIds(List<Staff> staffs){
        if(staffs == null) return new ArrayList<>();
        return staffs.stream()
                .filter(Objects::nonNull)
                .map(Staff::getId)
                .toList();
    }
}
