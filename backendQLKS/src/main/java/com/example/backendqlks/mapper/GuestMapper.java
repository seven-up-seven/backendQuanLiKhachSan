package com.example.backendqlks.mapper;

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
    @Mapping(target = "invoiceCreatedDates", source = "invoices", qualifiedByName = "invoicesToCreatedDates")
    @Mapping(target = "rentalFormDetailIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormDetailIds")
    @Mapping(target = "rentalFormIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormIds")
    @Mapping(target = "rentalFormCreatedDates", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormCreatedDates")
    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToBookingConfirmationFormIds")
    @Mapping(target = "bookingConfirmationFormCreatedDates", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToCreatedDates")
    @Mapping(target = "bookingConfirmationFormRoomIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToRoomIds")
    @Mapping(target = "bookingConfirmationFormRoomNames", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToRoomNames")
    ResponseGuestDto toResponseDto(Guest guest);
    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "invoicesToInvoiceIds")
    @Mapping(target = "invoiceCreatedDates", source = "invoices", qualifiedByName = "invoicesToCreatedDates")
    @Mapping(target = "rentalFormDetailIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormDetailIds")
    @Mapping(target = "rentalFormIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormIds")
    @Mapping(target = "rentalFormCreatedDates", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToRentalFormCreatedDates")
    @Mapping(target = "bookingConfirmationFormIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToBookingConfirmationFormIds")
    @Mapping(target = "bookingConfirmationFormCreatedDates", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToCreatedDates")
    @Mapping(target = "bookingConfirmationFormRoomIds", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToRoomIds")
    @Mapping(target = "bookingConfirmationFormRoomNames", source = "bookingConfirmationForms", qualifiedByName = "bookingConfirmationFormsToRoomNames")
    List<ResponseGuestDto> toResponseDtoList(List<Guest> guests);

    @Named(value = "invoicesToInvoiceIds")
    default List<Integer> invoicesToInvoiceIds(List<Invoice> invoices){
        if(invoices == null) return new ArrayList<>();
        return invoices.stream()
                .filter(Objects::nonNull)
                .map(Invoice::getId)
                .toList();
    }

    @Named(value = "invoicesToCreatedDates")
    default List<java.time.LocalDateTime> invoicesToCreatedDates(List<Invoice> invoices){
        if(invoices == null) return new ArrayList<>();
        return invoices.stream()
                .filter(Objects::nonNull)
                .map(Invoice::getCreatedAt)
                .toList();
    }

    @Named(value = "rentalFormDetailsToRentalFormDetailIds")
    default List<Integer> rentalFormDetailsToRentalFormDetailIds(List<RentalFormDetail> rentalFormDetails){
        if(rentalFormDetails == null) return new ArrayList<>();
        return rentalFormDetails.stream()
                .filter(Objects::nonNull)
                .map(RentalFormDetail::getId)
                .toList();
    }

    @Named(value = "rentalFormDetailsToRentalFormIds")
    default List<Integer> rentalFormDetailsToRentalFormIds(List<RentalFormDetail> rentalFormDetails){
        if(rentalFormDetails == null) return new ArrayList<>();
        return rentalFormDetails.stream()
                .filter(Objects::nonNull)
                .map(rfd -> rfd.getRentalForm() != null ? rfd.getRentalForm().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Named(value = "rentalFormDetailsToRentalFormCreatedDates")
    default List<java.time.LocalDateTime> rentalFormDetailsToRentalFormCreatedDates(List<RentalFormDetail> rentalFormDetails){
        if(rentalFormDetails == null) return new ArrayList<>();
        return rentalFormDetails.stream()
                .filter(Objects::nonNull)
                .map(rfd -> rfd.getRentalForm() != null ? rfd.getRentalForm().getCreatedAt() : null)
                .filter(Objects::nonNull)
                .distinct()
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

    @Named(value = "bookingConfirmationFormsToCreatedDates")
    default List<java.time.LocalDateTime> bookingConfirmationFormsToCreatedDates(List<BookingConfirmationForm> bookingConfirmationForms){
        if(bookingConfirmationForms == null) return new ArrayList<>();
        return bookingConfirmationForms.stream()
                .filter(Objects::nonNull)
                .map(BookingConfirmationForm::getCreatedAt)
                .toList();
    }

    @Named(value = "bookingConfirmationFormsToRoomIds")
    default List<String> bookingConfirmationFormsToRoomIds(List<BookingConfirmationForm> bookingConfirmationForms){
        if(bookingConfirmationForms == null) return new ArrayList<>();
        return bookingConfirmationForms.stream()
                .filter(Objects::nonNull)
                .map(bcf -> bcf.getRoom() != null ? String.valueOf(bcf.getRoom().getId()) : null)
                .filter(Objects::nonNull)
                .toList();
    }

    @Named(value = "bookingConfirmationFormsToRoomNames")
    default List<String> bookingConfirmationFormsToRoomNames(List<BookingConfirmationForm> bookingConfirmationForms){
        if(bookingConfirmationForms == null) return new ArrayList<>();
        return bookingConfirmationForms.stream()
                .filter(Objects::nonNull)
                .map(bcf -> bcf.getRoom() != null ? bcf.getRoom().getName() : null)
                .filter(Objects::nonNull)
                .toList();
    }
}
