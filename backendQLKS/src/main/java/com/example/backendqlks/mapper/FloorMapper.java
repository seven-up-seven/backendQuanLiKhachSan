package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.block.BlockDto;
import com.example.backendqlks.dto.block.ResponseBlockDto;
import com.example.backendqlks.dto.floor.FloorDto;
import com.example.backendqlks.dto.floor.ResponseFloorDto;
import com.example.backendqlks.entity.Block;
import com.example.backendqlks.entity.Floor;
import com.example.backendqlks.entity.Room;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface FloorMapper {
    Floor toEntity(FloorDto floorDto);
    FloorDto toDto(Floor floor);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(FloorDto floorDto, @MappingTarget Floor floor);

    @Mapping(target = "roomIds", source = "rooms", qualifiedByName = "roomsToRoomIds")
    ResponseFloorDto toResponseDto(Floor floor);
    @Mapping(target = "roomIds", source = "rooms", qualifiedByName = "roomsToRoomIds")
    List<ResponseFloorDto> toResponseDtoList(List<Floor> floors);

    @Named(value = "roomsToRoomIds")
    default List<Integer> roomsToRoomIds(List<Room> rooms){
        if(rooms == null) return new ArrayList<>();
        return rooms.stream()
                .filter(Objects::nonNull)
                .map(Room::getId)
                .toList();
    }
}
