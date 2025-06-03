package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.roomtype.ResponseRoomTypeDto;
import com.example.backendqlks.dto.roomtype.RoomTypeDto;
import com.example.backendqlks.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {
    RoomType toEntity(RoomTypeDto roomTypeDto);
    RoomTypeDto toDto(RoomType roomType);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomTypeDto roomTypeDto, @MappingTarget RoomType roomType);

    ResponseRoomTypeDto toResponseDto(RoomType roomType);
    List<ResponseRoomTypeDto> toResponseDtoList(List<RoomType> roomTypes);
}
