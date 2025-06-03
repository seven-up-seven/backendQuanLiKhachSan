package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.staff.ResponseStaffDto;
import com.example.backendqlks.dto.staff.StaffDto;
import com.example.backendqlks.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    Staff toEntity(StaffDto staffDto);
    StaffDto toDto(Staff staff);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(StaffDto staffDto, @MappingTarget Staff staff);

    ResponseStaffDto toResponseDto(Staff staff);
    List<ResponseStaffDto> toResponseDtoList(List<Staff> staffs);
}
