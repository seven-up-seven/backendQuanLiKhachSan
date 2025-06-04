package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.room.ResponseRoomDto;
import com.example.backendqlks.dto.room.RoomDto;
import com.example.backendqlks.entity.BookingConfirmationForm;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toEntity(RoomDto roomDto);
    RoomDto toDto(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomDto roomDto, @MappingTarget Room room);

    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormsToIds")
    ResponseRoomDto toResponseDto(Room room);
    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormsToIds")
    List<ResponseRoomDto> toResponseDtoList(List<Room> rooms);

    default List<Integer> bookingConfirmationFormsToIds(List<BookingConfirmationForm> bookingConfirmationForms) {
        if(bookingConfirmationForms == null) return new ArrayList<>();
        return bookingConfirmationForms.stream()
                .filter(Objects::nonNull)
                .map(BookingConfirmationForm::getId)
                .toList();
    }

    default List<Integer> rentalFormsToIds(List<RentalForm> rentalForms) {
        if(rentalForms == null) return new ArrayList<>();
        return rentalForms.stream()
                .filter(Objects::nonNull)
                .map(RentalForm::getId)
                .toList();
    }
}
