package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.staff.ResponseStaffDto;
import com.example.backendqlks.dto.staff.StaffDto;
import com.example.backendqlks.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    Staff toEntity(StaffDto staffDto);
    StaffDto toDto(Staff staff);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(StaffDto staffDto, @MappingTarget Staff staff);

    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "invoicesToIds")
    @Mapping(target = "rentalExtensionFormIds", source = "rentalExtensionForms", qualifiedByName = "rentalExtensionFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormIdsToIds")
    ResponseStaffDto toResponseDto(Staff staff);
    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "invoicesToIds")
    @Mapping(target = "rentalExtensionFormIds", source = "rentalExtensionForms", qualifiedByName = "rentalExtensionFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormIdsToIds")
    List<ResponseStaffDto> toResponseDtoList(List<Staff> staffs);

    default List<Integer> invoicesToIds(List<Invoice> invoices) {
        if(invoices == null) {
            return new ArrayList<>();
        }
        return invoices.stream()
                .filter(Objects::nonNull)
                .map(Invoice::getId)
                .toList();
    }

    default List<Integer> rentalExtensionFormsToIds(List<RentalExtensionForm> rentalExtensionForms) {
        if(rentalExtensionForms == null) {
            return new ArrayList<>();
        }
        return rentalExtensionForms.stream()
                .filter(Objects::nonNull)
                .map(RentalExtensionForm::getId)
                .toList();
    }

    default List<Integer> rentalFormIdsToIds(List<RentalForm> rentalForms) {
        if(rentalForms == null) {
            return new ArrayList<>();
        }
        return rentalForms.stream()
                .filter(Objects::nonNull)
                .map(RentalForm::getId)
                .toList();
    }
}
