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
    ResponseInvoiceDto toResponseDto(Invoice invoice);
    @Mapping(target = "invoiceDetailIds", source = "invoiceDetails", qualifiedByName = "invoiceDetailsToInvoiceDetailIds")
    List<ResponseInvoiceDto> toResponseDtoList(List<Invoice> invoices);

    @Named(value = "invoiceDetailsToInvoiceDetailIds")
    default List<Integer> invoiceDetailsToInvoiceDetailIds(List<InvoiceDetail> invoiceDetails){
        if(invoiceDetails == null) return new ArrayList<>();
        return invoiceDetails.stream()
                .filter(Objects::nonNull)
                .map(InvoiceDetail::getId)
                .toList();
    }
}
