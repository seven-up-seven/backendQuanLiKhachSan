package com.example.backendqlks.service;

import com.example.backendqlks.dao.BlockRepository;
import com.example.backendqlks.dao.FloorRepository;
import com.example.backendqlks.dto.block.BlockDto;
import com.example.backendqlks.dto.block.ResponseBlockDto;
import com.example.backendqlks.dto.floor.FloorDto;
import com.example.backendqlks.dto.floor.ResponseFloorDto;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.entity.enums.Action;
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
    private final HistoryService historyService;

    public FloorService(FloorRepository floorRepository,
                        FloorMapper floorMapper,
                        BlockRepository blockRepository,
                        HistoryService historyService) {
        this.floorMapper = floorMapper;
        this.floorRepository = floorRepository;
        this.blockRepository = blockRepository;
        this.historyService = historyService;
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
    public ResponseFloorDto create(FloorDto floorDto, int impactorId, String impactor){
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
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tầng")
                .affectedObjectId(floor.getId())
                .action(Action.CREATE)
                .content("Tên tầng: " + floor.getName() + "; Block ID: " + floor.getBlockId())
                .build();
        historyService.create(history);
        return floorMapper.toResponseDto(floor);
    }

    //TODO: add try catch
    public ResponseFloorDto update(int floorId, FloorDto floorDto, int impactorId, String impactor){
        var existingFloor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        StringBuilder contentBuilder = new StringBuilder();
        if (!existingFloor.getName().equals(floorDto.getName())) {
            contentBuilder.append("Tên tầng: ")
                    .append(existingFloor.getName())
                    .append(" -> ")
                    .append(floorDto.getName())
                    .append("; ");
        }
        if (existingFloor.getBlockId() != floorDto.getBlockId()) {
            contentBuilder.append("Block ID: ")
                    .append(existingFloor.getBlockId())
                    .append(" -> ")
                    .append(floorDto.getBlockId())
                    .append("; ");
        }
        floorMapper.updateEntityFromDto(floorDto, existingFloor);
        floorRepository.save(existingFloor);
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tầng")
                .affectedObjectId(existingFloor.getId())
                .action(Action.UPDATE)
                .content(contentBuilder.toString())
                .build();
        historyService.create(history);
        return floorMapper.toResponseDto(existingFloor);
    }

    //TODO: modify later
    public void delete(int floorId, int impactorId, String impactor){
        var existingFloor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tầng")
                .affectedObjectId(existingFloor.getId())
                .action(Action.DELETE)
                .content("Tên tầng: " + existingFloor.getName() + "; Block ID: " + existingFloor.getBlockId())
                .build();
        historyService.create(history);
        floorRepository.delete(existingFloor);
    }
}
