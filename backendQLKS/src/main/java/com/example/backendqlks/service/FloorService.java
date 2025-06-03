package com.example.backendqlks.service;

import com.example.backendqlks.dao.BlockRepository;
import com.example.backendqlks.dao.FloorRepository;
import com.example.backendqlks.dto.block.BlockDto;
import com.example.backendqlks.dto.block.ResponseBlockDto;
import com.example.backendqlks.dto.floor.FloorDto;
import com.example.backendqlks.dto.floor.ResponseFloorDto;
import com.example.backendqlks.mapper.BlockMapper;
import com.example.backendqlks.mapper.FloorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class FloorService {
    private final FloorRepository floorRepository;
    private final BlockRepository blockRepository;
    private final FloorMapper floorMapper;

    public FloorService(FloorRepository floorRepository,
                        FloorMapper floorMapper,
                        BlockRepository blockRepository){
        this.floorMapper = floorMapper;
        this.floorRepository = floorRepository;
        this.blockRepository = blockRepository;
    }

    @Transactional(readOnly = true)
    public ResponseFloorDto get(int floorId){
        var existingFloor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        return floorMapper.toResponseDto(existingFloor);
    }
    @Transactional(readOnly = true)
    public List<ResponseFloorDto> getAll(){
        var allFloor = floorRepository.findAll();
        return floorMapper.toResponseDtoList(allFloor);
    }
    //TODO: add try catch
    public ResponseFloorDto create(FloorDto floorDto){
        var blockContains = blockRepository.findById(floorDto.getBlockId());
        if(blockContains.isEmpty()){
            throw new IllegalArgumentException("Incorrect block id");
        }
        if(blockContains.get().getFloors().stream()
                .filter(Objects::nonNull).anyMatch(floor->
                        floor.getName().equalsIgnoreCase(floorDto.getName()))){
            throw new IllegalArgumentException("This block already contained a floor with same name");
        }
        var floor = floorMapper.toEntity(floorDto);
        floorRepository.save(floor);
        return floorMapper.toResponseDto(floor);
    }
    //TODO: add try catch
    public ResponseFloorDto update(int floorId, FloorDto floorDto){
        var existingFloor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        floorMapper.updateEntityFromDto(floorDto, existingFloor);
        floorRepository.save(existingFloor);
        return floorMapper.toResponseDto(existingFloor);
    }
    //TODO: modify later
    public void delete(int floorId){
        var existingFloor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        floorRepository.delete(existingFloor);
    }
}
