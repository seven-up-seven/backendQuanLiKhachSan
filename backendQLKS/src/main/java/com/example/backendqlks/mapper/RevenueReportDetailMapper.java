package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.revenuereportdetail.ResponseRevenueReportDetailDto;
import com.example.backendqlks.dto.revenuereportdetail.RevenueReportDetailDto;
import com.example.backendqlks.entity.RevenueReportDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RevenueReportDetailMapper {
    RevenueReportDetail toEntity(RevenueReportDetailDto revenueReportDetailDto);
    RevenueReportDetailDto toDto(RevenueReportDetail revenueReportDetail);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RevenueReportDetailDto revenueReportDetailDto, @MappingTarget RevenueReportDetail revenueReportDetail);

    @Mapping(target = "revenueReportId", source = "revenueReport.id")
    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomTypeName", source = "roomType.name")
    ResponseRevenueReportDetailDto toResponseDto(RevenueReportDetail revenueReportDetail);

    @Mapping(target = "revenueReportId", source = "revenueReport.id")
    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomTypeName", source = "roomType.name")
    List<ResponseRevenueReportDetailDto> toResponseDtoList(List<RevenueReportDetail> revenueReportDetails);
}
