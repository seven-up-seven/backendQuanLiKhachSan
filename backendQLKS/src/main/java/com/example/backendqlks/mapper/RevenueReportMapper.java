package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.revenuereport.ResponseRevenueReportDto;
import com.example.backendqlks.dto.revenuereport.RevenueReportDto;
import com.example.backendqlks.entity.RevenueReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RevenueReportMapper {
    RevenueReport toEntity(RevenueReportDto revenueReportDto);
    RevenueReportDto toDto(RevenueReport revenueReport);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RevenueReportDto revenueReportDto, @MappingTarget RevenueReport revenueReport);

    ResponseRevenueReportDto toResponseDto(RevenueReport revenueReport);
    List<ResponseRevenueReportDto> toResponseDtoList(List<RevenueReport> revenueReports);
}
