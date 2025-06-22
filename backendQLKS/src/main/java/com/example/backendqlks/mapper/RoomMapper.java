package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.room.ResponseRoomDto;
import com.example.backendqlks.dto.room.RoomDto;
import com.example.backendqlks.entity.BookingConfirmationForm;
import com.example.backendqlks.entity.ImageEntity;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(RoomDto roomDto);
    RoomDto toDto(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomDto roomDto, @MappingTarget Room room);

    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomTypeName", source = "roomType.name")
    @Mapping(target = "floorId", source = "floor.id")
    @Mapping(target = "floorName", source = "floor.name")
    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormsToIds")
    @Mapping(target = "imageIds", source = "images", qualifiedByName = "imagesToIds")
    ResponseRoomDto toResponseDto(Room room);

    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomTypeName", source = "roomType.name")
    @Mapping(target = "floorId", source = "floor.id")
    @Mapping(target = "floorName", source = "floor.name")
    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormsToIds")
    @Mapping(target = "imageIds", source = "images", qualifiedByName = "imagesToIds")
    List<ResponseRoomDto> toResponseDtoList(List<Room> rooms);

    @Named(value = "bookingConfirmationFormsToIds")
    default List<Integer> bookingConfirmationFormsToIds(List<BookingConfirmationForm> bookingConfirmationForms) {
        if(bookingConfirmationForms == null) return new ArrayList<>();
        return bookingConfirmationForms.stream()
                .filter(Objects::nonNull)
                .map(BookingConfirmationForm::getId)
                .toList();
    }

    @Named(value = "rentalFormsToIds")
    default List<Integer> rentalFormsToIds(List<RentalForm> rentalForms) {
        if(rentalForms == null) return new ArrayList<>();
        return rentalForms.stream()
                .filter(Objects::nonNull)
                .map(RentalForm::getId)
                .toList();
    }

    @Named(value="imagesToIds")
    default List<Integer> imagesToIds(List<ImageEntity> imageEntities) {
        if (imageEntities == null) return new ArrayList<>();
        return imageEntities.stream()
                .filter(Objects::nonNull)
                .map(ImageEntity::getId)
                .toList();
    }
}
