package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.guest.GuestDto;
import com.example.backendqlks.dto.guest.ResponseGuestDto;
import com.example.backendqlks.dto.invoice.InvoiceDto;
import com.example.backendqlks.dto.invoice.ResponseInvoiceDto;
import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.InvoiceDetail;
import com.example.backendqlks.entity.RentalFormDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice toEntity(InvoiceDto invoiceDto);
    InvoiceDto toDto(Invoice invoice);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(InvoiceDto invoiceDto, @MappingTarget Invoice invoice);
    @Mapping(target = "invoiceDetailIds", source = "invoiceDetails", qualifiedByName = "invoiceDetailsToInvoiceDetailIds")
    @Mapping(target = "payingGuestName", source = "payingGuest", qualifiedByName = "toGuestName")
    @Mapping(target = "payingGuestId", source = "payingGuest", qualifiedByName = "toGuestId")
    @Mapping(target = "staffName", source = "staff", qualifiedByName = "toStaffName")
    @Mapping(target = "staffId", source = "staff", qualifiedByName = "toStaffId")
    @Mapping(target = "rentalFormIds", source = "invoiceDetails", qualifiedByName = "invoiceDetailsToRentalFormIds")
    ResponseInvoiceDto toResponseDto(Invoice invoice);
    @Mapping(target = "invoiceDetailIds", source = "invoiceDetails", qualifiedByName = "invoiceDetailsToInvoiceDetailIds")
    @Mapping(target = "payingGuestName", source = "payingGuest", qualifiedByName = "toGuestName")
    @Mapping(target = "payingGuestId", source = "payingGuest", qualifiedByName = "toGuestId")
    @Mapping(target = "staffName", source = "staff", qualifiedByName = "toStaffName")
    @Mapping(target = "staffId", source = "staff", qualifiedByName = "toStaffId")
    @Mapping(target = "rentalFormIds", source = "invoiceDetails", qualifiedByName = "invoiceDetailsToRentalFormIds")
    List<ResponseInvoiceDto> toResponseDtoList(List<Invoice> invoices);

    @Named(value = "invoiceDetailsToInvoiceDetailIds")
    default List<Integer> invoiceDetailsToInvoiceDetailIds(List<InvoiceDetail> invoiceDetails){
        if(invoiceDetails == null) return new ArrayList<>();
        return invoiceDetails.stream()
                .filter(Objects::nonNull)
                .map(InvoiceDetail::getId)
                .toList();
    }

    @Named("toGuestName")
    default String toGuestName(com.example.backendqlks.entity.Guest guest) {
        return guest != null ? guest.getName() : null;
    }

    @Named("toGuestId")
    default int toGuestId(com.example.backendqlks.entity.Guest guest) {
        return guest != null ? guest.getId() : 0;
    }

    @Named("toStaffName")
    default String toStaffName(com.example.backendqlks.entity.Staff staff) {
        return staff != null ? staff.getFullName() : null;
    }

    @Named("toStaffId")
    default int toStaffId(com.example.backendqlks.entity.Staff staff) {
        return staff != null ? staff.getId() : 0;
    }

    @Named("invoiceDetailsToRentalFormIds")
    default List<Integer> invoiceDetailsToRentalFormIds(List<InvoiceDetail> invoiceDetails) {
        if(invoiceDetails == null) return new ArrayList<>();
        return invoiceDetails.stream()
                .filter(Objects::nonNull)
                .map(InvoiceDetail::getRentalFormId)
                .distinct()
                .toList();
    }
}
