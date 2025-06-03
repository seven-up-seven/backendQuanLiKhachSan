package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.revenuereportdetail.ResponseRevenueReportDetailDto;
import com.example.backendqlks.dto.revenuereportdetail.RevenueReportDetailDto;
import com.example.backendqlks.entity.RevenueReportDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RevenueReportDetailMapper {
    RevenueReportDetail toEntity(RevenueReportDetailDto revenueReportDetailDto);
    RevenueReportDetailDto toDto(RevenueReportDetail revenueReportDetail);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RevenueReportDetailDto revenueReportDetailDto, @MappingTarget RevenueReportDetail revenueReportDetail);

    ResponseRevenueReportDetailDto toResponseDto(RevenueReportDetail revenueReportDetail);
    List<ResponseRevenueReportDetailDto> toResponseDtoList(List<RevenueReportDetail> revenueReportDetails);
}
