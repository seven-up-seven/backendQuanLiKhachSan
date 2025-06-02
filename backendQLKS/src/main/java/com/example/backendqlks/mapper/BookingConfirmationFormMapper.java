package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.bookingConfirmationForm.BookingConfirmationFormDto;
import com.example.backendqlks.dto.bookingConfirmationForm.ResponseBookingConfirmationFormDto;
import com.example.backendqlks.entity.BookingConfirmationForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingConfirmationFormMapper {
    BookingConfirmationForm toEntity(BookingConfirmationFormDto bookingConfirmationFormDto);
    BookingConfirmationFormDto toDto(BookingConfirmationForm bookingConfirmationForm);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BookingConfirmationFormDto bookingConfirmationFormDto, @MappingTarget BookingConfirmationForm bookingConfirmationForm);

    ResponseBookingConfirmationFormDto toResponseDto(BookingConfirmationForm bookingConfirmationForm);
    List<ResponseBookingConfirmationFormDto> toResponseDtoList(List<BookingConfirmationForm> bookingConfirmationForms);
}
