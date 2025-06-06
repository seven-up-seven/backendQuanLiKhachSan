package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.rentalformdetail.RentalFormDetailDto;
import com.example.backendqlks.dto.rentalformdetail.ResponseRentalFormDetailDto;
import com.example.backendqlks.entity.RentalFormDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalFormDetailMapper {
    RentalFormDetail toEntity(RentalFormDetailDto rentalFormDetailDto);
    RentalFormDetailDto toDto(RentalFormDetail rentalFormDetail);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RentalFormDetailDto rentalFormDto, @MappingTarget RentalFormDetail rentalForm);

    @Mapping(target = "rentalFormId", source = "rentalForm.id")
    @Mapping(target = "guestId", source = "guest.id")
    @Mapping(target = "guestName", source = "guest.name")
    @Mapping(target = "guestPhoneNumber", source = "guest.phoneNumber")
    @Mapping(target = "guestEmail", source = "guest.email")
    @Mapping(target = "guestIdentificationNumber", source = "guest.identificationNumber")
    ResponseRentalFormDetailDto toResponseDto(RentalFormDetail rentalFormDetail);

    @Mapping(target = "rentalFormId", source = "rentalForm.id")
    @Mapping(target = "guestId", source = "guest.id")
    @Mapping(target = "guestName", source = "guest.name")
    @Mapping(target = "guestPhoneNumber", source = "guest.phoneNumber")
    @Mapping(target = "guestEmail", source = "guest.email")
    @Mapping(target = "guestIdentificationNumber", source = "guest.identificationNumber")
    List<ResponseRentalFormDetailDto> toResponseDtoList(List<RentalFormDetail> rentalFormDetails);
}
