package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.invoice.InvoiceDto;
import com.example.backendqlks.dto.invoice.ResponseInvoiceDto;
import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.dto.invoiceDetail.ResponseInvoiceDetailDto;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.InvoiceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceDetailMapper {
    InvoiceDetail toEntity(InvoiceDetailDto invoiceDetailDto);
    InvoiceDetailDto toDto(InvoiceDetail invoiceDetail);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(InvoiceDetailDto invoiceDetailDto, @MappingTarget InvoiceDetail invoiceDetail);

    @Mapping(target = "invoiceId", source = "invoice.id")
    @Mapping(target = "rentalFormId", source = "rentalForm.id")
    @Mapping(target = "roomId", source = "rentalForm", qualifiedByName = "toRoomId")
    ResponseInvoiceDetailDto toResponseDto(InvoiceDetail invoiceDetail);
    @Mapping(target = "invoiceId", source = "invoice.id")
    @Mapping(target = "rentalFormId", source = "rentalForm.id")
    @Mapping(target = "roomId", source = "rentalForm", qualifiedByName = "toRoomId")
    List<ResponseInvoiceDetailDto> toResponseDtoList(List<InvoiceDetail> invoiceDetails);

    @Named("toRoomId")
    default int toRoomId(com.example.backendqlks.entity.RentalForm rentalForm) {
        return rentalForm != null ? rentalForm.getRoomId() : 0;
    }
}
