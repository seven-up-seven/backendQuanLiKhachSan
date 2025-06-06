package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.rentalextensionform.RentalExtensionFormDto;
import com.example.backendqlks.dto.rentalextensionform.ResponseRentalExtensionFormDto;
import com.example.backendqlks.entity.RentalExtensionForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalExtensionFormMapper {
    RentalExtensionForm toEntity(RentalExtensionFormDto rentalExtensionFormDto);
    RentalExtensionFormDto toDto(RentalExtensionForm rentalExtensionForm);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RentalExtensionFormDto rentalExtensionFormDto, @MappingTarget RentalExtensionForm rentalExtensionForm);

    @Mapping(target = "id", source = "rentalExtensionForm.id")
    @Mapping(target = "rentalFormId", source = "rentalForm.id")
    @Mapping(target = "rentalFormRoomName", source = "rentalForm", qualifiedByName = "toRoomName")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.fullName")
    ResponseRentalExtensionFormDto toResponseDto(RentalExtensionForm rentalExtensionForm);
    @Mapping(target = "id", source = "rentalExtensionForm.id")
    @Mapping(target = "rentalFormId", source = "rentalForm.id")
    @Mapping(target = "rentalFormRoomName", source = "rentalForm", qualifiedByName = "toRoomName")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.fullName")
    List<ResponseRentalExtensionFormDto> toResponseDtoList(List<RentalExtensionForm> rentalExtensionForms);

    @Named("toRoomName")
    default String toRoomName(com.example.backendqlks.entity.RentalForm rentalForm) {
        return rentalForm != null && rentalForm.getRoom() != null ? rentalForm.getRoom().getName() : null;
    }
}
