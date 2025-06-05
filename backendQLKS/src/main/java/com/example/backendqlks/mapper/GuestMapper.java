package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.floor.FloorDto;
import com.example.backendqlks.dto.floor.ResponseFloorDto;
import com.example.backendqlks.dto.guest.GuestDto;
import com.example.backendqlks.dto.guest.ResponseGuestDto;
import com.example.backendqlks.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    Guest toEntity(GuestDto guestDto);
    GuestDto toDto(Guest guest);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(GuestDto guestDto, @MappingTarget Guest guest);
    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "invoicesToInvoiceIds")
    @Mapping(target = "rentalFormDetailIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormDetailIds")
    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToBookingConfirmationFormIds")
    ResponseGuestDto toResponseDto(Guest guest);
    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "invoicesToInvoiceIds")
    @Mapping(target = "rentalFormDetailIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormDetailIds")
    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToBookingConfirmationFormIds")
    List<ResponseGuestDto> toResponseDtoList(List<Guest> guests);

    @Named(value = "invoicesToInvoiceIds")
    default List<Integer> invoicesToInvoiceIds(List<Invoice> invoices){
        if(invoices == null) return new ArrayList<>();
        return invoices.stream()
                .filter(Objects::nonNull)
                .map(Invoice::getId)
                .toList();
    }

    @Named(value = "rentalFormDetailsToRentalFormDetailIds")
    default List<Integer> rentalFormDetailToRentalFormDetailIds(List<RentalFormDetail> rentalFormDetails){
        if(rentalFormDetails == null) return new ArrayList<>();
        return rentalFormDetails.stream()
                .filter(Objects::nonNull)
                .map(RentalFormDetail::getId)
                .toList();
    }

    @Named(value = "bookingConfirmationFormsToBookingConfirmationFormIds")
    default List<Integer> bookingConfirmationFormsToBookingConfirmationFormIds(List<BookingConfirmationForm> bookingConfirmationForms){
        if(bookingConfirmationForms == null) return new ArrayList<>();
        return bookingConfirmationForms.stream()
                .filter(Objects::nonNull)
                .map(BookingConfirmationForm::getId)
                .toList();
    }
}
