package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.image.ImageDto;
import com.example.backendqlks.dto.image.ResponseImageDto;
import com.example.backendqlks.entity.ImageEntity;
import com.example.backendqlks.entity.Room;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "room", source = "roomId", qualifiedByName = "mapRoomIdToRoom")
    @Mapping(target = "id", ignore = true) // Bỏ qua id khi tạo mới entity
    @Mapping(target = "uploadedAt", ignore = true) // Bỏ qua uploadedAt vì được set trong @PrePersist
    ImageEntity toEntity(ImageDto dto);

    @Mapping(target = "roomId", source = "room.id")
    ImageDto toDto(ImageEntity entity);

    @Mapping(target = "roomId", source = "room.id")
    ResponseImageDto toResponseDto(ImageEntity entity);

    @Named("mapRoomIdToRoom")
    default Room mapRoomIdToRoom(Integer roomId) {
        if (roomId == null) return null;
        Room room = new Room();
        room.setId(roomId);
        return room;
    }
}