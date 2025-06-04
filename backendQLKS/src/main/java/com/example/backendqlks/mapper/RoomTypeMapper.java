package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.roomtype.ResponseRoomTypeDto;
import com.example.backendqlks.dto.roomtype.RoomTypeDto;
import com.example.backendqlks.entity.RevenueReportDetail;
import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {
    RoomType toEntity(RoomTypeDto roomTypeDto);
    RoomTypeDto toDto(RoomType roomType);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RoomTypeDto roomTypeDto, @MappingTarget RoomType roomType);

    @Mapping(target = "roomIds", source = "rooms", qualifiedByName = "roomsToIds")
    @Mapping(target = "revenueReportDetailIds", source = "revenueReportDetails", qualifiedByName = "revenueReportDetailsToIds")
    ResponseRoomTypeDto toResponseDto(RoomType roomType);
    @Mapping(target = "roomIds", source = "rooms", qualifiedByName = "roomsToIds")
    @Mapping(target = "revenueReportDetailIds", source = "revenueReportDetails", qualifiedByName = "revenueReportDetailsToIds")
    List<ResponseRoomTypeDto> toResponseDtoList(List<RoomType> roomTypes);

    default List<Integer> roomsToIds(List<Room> roomIds) {
        if(roomIds == null) return new ArrayList<>();
        return roomIds.stream()
                .filter(Objects::nonNull)
                .map(Room::getId)
                .toList();
    }

    default List<Integer> revenueReportDetailsToIds(List<RevenueReportDetail> revenueReportDetails) {
        if(revenueReportDetails == null) return new ArrayList<>();
        return revenueReportDetails.stream()
                .filter(Objects::nonNull)
                .map(RevenueReportDetail::getId)
                .toList();
    }
}
