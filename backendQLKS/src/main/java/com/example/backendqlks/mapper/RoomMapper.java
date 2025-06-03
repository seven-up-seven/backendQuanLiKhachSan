package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.room.ResponseRoomDto;
import com.example.backendqlks.dto.room.RoomDto;
import com.example.backendqlks.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(RoomDto roomDto);
    RoomDto toDto(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomDto roomDto, @MappingTarget Room room);

    ResponseRoomDto toResponseDto(Room room);
    List<ResponseRoomDto> toResponseDtoList(List<Room> rooms);
}
