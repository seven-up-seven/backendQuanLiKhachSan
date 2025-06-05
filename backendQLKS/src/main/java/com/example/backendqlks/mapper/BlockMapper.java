package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.block.BlockDto;
import com.example.backendqlks.dto.block.ResponseBlockDto;
import com.example.backendqlks.entity.Block;
import com.example.backendqlks.entity.Floor;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BlockMapper {
    Block toEntity(BlockDto blockDto);
    BlockDto toDto(Block block);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BlockDto blockDto, @MappingTarget Block block);

    @Mapping(target = "floorIds", source = "floors", qualifiedByName = "floorsToFloorIds")
    ResponseBlockDto toResponseDto(Block block);
    @Mapping(target = "floorIds", source = "floors", qualifiedByName = "floorsToFloorIds")
    List<ResponseBlockDto> toResponseDtoList(List<Block> blocks);

    @Named(value = "floorsToFloorIds")
    default List<Integer> floorsToFloorIds(List<Floor> floors){
        if(floors == null) return new ArrayList<>(); // return an empty list instead
        return floors.stream()
                .filter(Objects::nonNull)
                .map(Floor::getId)
                .toList();
    }
}
