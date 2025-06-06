package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.RentalFormDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RentalFormMapper {
    RentalForm toEntity(RentalFormDto rentalFormDto);
    RentalFormDto toDto(RentalForm rentalForm);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RentalFormDto rentalFormDto, @MappingTarget RentalForm rentalForm);

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "roomName", source = "room.name")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.fullName")
    @Mapping(target = "rentalFormDetailIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToIds")
    @Mapping(target = "rentalExtensionFormIds", source = "rentalExtensionForms", qualifiedByName = "rentalExtensionFormToIds")
    ResponseRentalFormDto toResponseDto(RentalForm rentalForm);

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "roomName", source = "room.name")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.fullName")
    @Mapping(target = "rentalFormDetailIds", source = "rentalFormDetails", qualifiedByName = "rentalFormDetailsToIds")
    @Mapping(target = "rentalExtensionFormIds", source = "rentalExtensionForms", qualifiedByName = "rentalExtensionFormToIds")
    List<ResponseRentalFormDto> toResponseDtoList(List<RentalForm> rentalForms);

    @Named(value = "rentalFormDetailsToIds")
    default List<Integer> rentalFormDetailsToIds(List<RentalFormDetail> rentalFormDetails) {
        if(rentalFormDetails == null) return new ArrayList<>();
        return rentalFormDetails.stream().filter(Objects::nonNull)
                .map(RentalFormDetail::getId)
                .toList();
    }

    @Named(value = "rentalExtensionFormToIds")
    default List<Integer> rentalExtensionFormToIds(List<RentalExtensionForm> rentalExtensionForms) {
        if (rentalExtensionForms == null) return new ArrayList<>();
        return rentalExtensionForms.stream()
                .filter(Objects::nonNull)
                .map(RentalExtensionForm::getId)
                .toList();
    }
}
