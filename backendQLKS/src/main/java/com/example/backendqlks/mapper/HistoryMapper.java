package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.history.ResponseHistoryDto;
import com.example.backendqlks.entity.History;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    HistoryDto toDto(History history);
    History toEntity(HistoryDto historyDto);

    void updateEntityFromDto(HistoryDto historyDto, @MappingTarget History history);

    ResponseHistoryDto toResponseDto(History history);
    List<ResponseHistoryDto> toResponseDtoList(List<History> histories);
}
