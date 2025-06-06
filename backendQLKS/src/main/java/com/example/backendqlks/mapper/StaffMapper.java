package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.staff.ResponseStaffDto;
import com.example.backendqlks.dto.staff.StaffDto;
import com.example.backendqlks.entity.*;
import jakarta.persistence.NamedEntityGraph;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    Staff toEntity(StaffDto staffDto);
    StaffDto toDto(Staff staff);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(StaffDto staffDto, @MappingTarget Staff staff);

    @Mapping(target = "positionId", source = "position.id")
    @Mapping(target = "positionName", source = "position.name")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountUsername", source = "account.username")
    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "invoicesToIds")
    @Mapping(target = "rentalExtensionFormIds", source = "rentalExtensionForms", qualifiedByName = "rentalExtensionFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormIdsToIds")
    ResponseStaffDto toResponseDto(Staff staff);

    @Mapping(target = "positionId", source = "position.id")
    @Mapping(target = "positionName", source = "position.name")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountUsername", source = "account.username")
    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "invoicesToIds")
    @Mapping(target = "rentalExtensionFormIds", source = "rentalExtensionForms", qualifiedByName = "rentalExtensionFormsToIds")
    @Mapping(target = "rentalFormIds", source = "rentalForms", qualifiedByName = "rentalFormIdsToIds")
    List<ResponseStaffDto> toResponseDtoList(List<Staff> staffs);

    @Named(value = "invoicesToIds")
    default List<Integer> invoicesToIds(List<Invoice> invoices) {
        if(invoices == null) {
            return new ArrayList<>();
        }
        return invoices.stream()
                .filter(Objects::nonNull)
                .map(Invoice::getId)
                .toList();
    }

    @Named(value = "rentalExtensionFormsToIds")
    default List<Integer> rentalExtensionFormsToIds(List<RentalExtensionForm> rentalExtensionForms) {
        if(rentalExtensionForms == null) {
            return new ArrayList<>();
        }
        return rentalExtensionForms.stream()
                .filter(Objects::nonNull)
                .map(RentalExtensionForm::getId)
                .toList();
    }

    @Named(value = "rentalFormIdsToIds")
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
