package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.rentalextensionform.RentalExtensionFormDto;
import com.example.backendqlks.dto.rentalextensionform.ResponseRentalExtensionFormDto;
import com.example.backendqlks.entity.RentalExtensionForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalExtensionFormMapper {
    RentalExtensionForm toEntity(RentalExtensionFormDto rentalExtensionFormDto);
    RentalExtensionFormDto toDto(RentalExtensionForm rentalExtensionForm);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RentalExtensionFormDto rentalExtensionFormDto, @MappingTarget RentalExtensionForm rentalExtensionForm);

    ResponseRentalExtensionFormDto toResponseDto(RentalExtensionForm rentalExtensionForm);
    List<ResponseRentalExtensionFormDto> toResponseDtoList(List<RentalExtensionForm> rentalExtensionForms);
}
