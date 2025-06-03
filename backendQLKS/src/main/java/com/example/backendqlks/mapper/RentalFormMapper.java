package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.entity.RentalForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalFormMapper {
    RentalForm toEntity(RentalFormDto rentalFormDto);
    RentalFormDto toDto(RentalForm rentalForm);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RentalFormDto rentalFormDto, @MappingTarget RentalForm rentalForm);

    ResponseRentalFormDto toResponseDto(RentalForm rentalForm);
    List<ResponseRentalFormDto> toResponseDtoList(List<RentalForm> rentalForms);
}
