package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.bookingConfirmationForm.BookingConfirmationFormDto;
import com.example.backendqlks.dto.bookingConfirmationForm.ResponseBookingConfirmationFormDto;
import com.example.backendqlks.entity.BookingConfirmationForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingConfirmationFormMapper {
    BookingConfirmationForm toEntity(BookingConfirmationFormDto bookingConfirmationFormDto);
    BookingConfirmationFormDto toDto(BookingConfirmationForm bookingConfirmationForm);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BookingConfirmationFormDto bookingConfirmationFormDto, @MappingTarget BookingConfirmationForm bookingConfirmationForm);

    @Mapping(target = "guestName", source = "bookingGuest", qualifiedByName = "toGuestName")
    @Mapping(target = "guestEmail", source = "bookingGuest", qualifiedByName = "toGuestEmail")
    @Mapping(target = "guestPhoneNumber", source = "bookingGuest", qualifiedByName = "toGuestPhoneNumber")
    @Mapping(target = "guestId", source = "bookingGuest", qualifiedByName = "toGuestId")
    @Mapping(target = "guestIdentificationNumber", source = "bookingGuest", qualifiedByName = "toGuestIdentificationNumber")
    @Mapping(target = "roomName", source = "room", qualifiedByName = "toRoomName")
    @Mapping(target = "roomTypeName", source = "room", qualifiedByName = "toRoomTypeName")
    @Mapping(target = "bookingDate", source = "bookingDate")
    @Mapping(target = "rentalDays", source = "rentalDays")
    ResponseBookingConfirmationFormDto toResponseDto(BookingConfirmationForm bookingConfirmationForm);

    @Mapping(target = "guestName", source = "bookingGuest", qualifiedByName = "toGuestName")
    @Mapping(target = "guestEmail", source = "bookingGuest", qualifiedByName = "toGuestEmail")
    @Mapping(target = "guestPhoneNumber", source = "bookingGuest", qualifiedByName = "toGuestPhoneNumber")
    @Mapping(target = "guestId", source = "bookingGuest", qualifiedByName = "toGuestId")
    @Mapping(target = "guestIdentificationNumber", source = "bookingGuest", qualifiedByName = "toGuestIdentificationNumber")
    @Mapping(target = "roomName", source = "room", qualifiedByName = "toRoomName")
    @Mapping(target = "roomTypeName", source = "room", qualifiedByName = "toRoomTypeName")
    List<ResponseBookingConfirmationFormDto> toResponseDtoList(List<BookingConfirmationForm> bookingConfirmationForms);

    @Named("toGuestName")
    default String toGuestName(com.example.backendqlks.entity.Guest guest) {
        return guest != null ? guest.getName() : null;
    }

    @Named("toGuestEmail")
    default String toGuestEmail(com.example.backendqlks.entity.Guest guest) {
        return guest != null ? guest.getEmail() : null;
    }

    @Named("toGuestPhoneNumber")
    default String toGuestPhoneNumber(com.example.backendqlks.entity.Guest guest) {
        return guest != null ? guest.getPhoneNumber() : null;
    }

    @Named("toGuestId")
    default int toGuestId(com.example.backendqlks.entity.Guest guest) {
        return guest != null ? guest.getId() : 0;
    }

    @Named("toGuestIdentificationNumber")
    default String toGuestIdentificationNumber(com.example.backendqlks.entity.Guest guest) {
        return guest != null ? guest.getIdentificationNumber() : null;
    }

    @Named("toRoomName")
    default String toRoomName(com.example.backendqlks.entity.Room room) {
        return room != null ? room.getName() : null;
    }

    @Named("toRoomTypeName")
    default String toRoomTypeName(com.example.backendqlks.entity.Room room) {
        return room != null && room.getRoomType() != null ? room.getRoomType().getName() : null;
    }
}
