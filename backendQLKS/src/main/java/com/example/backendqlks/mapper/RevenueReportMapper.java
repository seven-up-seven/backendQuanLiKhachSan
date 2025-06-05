package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.revenuereport.ResponseRevenueReportDto;
import com.example.backendqlks.dto.revenuereport.RevenueReportDto;
import com.example.backendqlks.entity.RevenueReport;
import com.example.backendqlks.entity.RevenueReportDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RevenueReportMapper {
    RevenueReport toEntity(RevenueReportDto revenueReportDto);
    RevenueReportDto toDto(RevenueReport revenueReport);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RevenueReportDto revenueReportDto, @MappingTarget RevenueReport revenueReport);

    @Mapping(target = "revenueReportDetailIds", source = "revenueReportDetails", qualifiedByName = "revenueReportDetailsToListIds")
    ResponseRevenueReportDto toResponseDto(RevenueReport revenueReport);
    @Mapping(target = "revenueReportDetailIds", source = "revenueReportDetails", qualifiedByName = "revenueReportDetailsToListIds")
    List<ResponseRevenueReportDto> toResponseDtoList(List<RevenueReport> revenueReports);

    @Named(value = "revenueReportDetailsToListIds")
    default List<Integer> revenueReportDetailsToListIds(List<RevenueReportDetail> revenueReportDetails){
        if(revenueReportDetails==null) return new ArrayList<>();
        return revenueReportDetails.stream().filter(Objects::nonNull)
                .map(RevenueReportDetail::getId)
                .toList();
    }
}
