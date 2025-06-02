package com.example.backendqlks.service;

import com.example.backendqlks.dao.PositionRepository;
import com.example.backendqlks.dto.position.PositionDto;
import com.example.backendqlks.dto.position.ResponsePositionDto;
import com.example.backendqlks.mapper.PositionMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Transactional
@Service
public class PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository,
                           PositionMapper positionMapper){
        this.positionMapper = positionMapper;
        this.positionRepository = positionRepository;
    }

    @Transactional(readOnly = true)
    public ResponsePositionDto get(int positionId){
        var existingPosition = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect position id"));
        return positionMapper.toResponseDto(existingPosition);
    }

    @Transactional(readOnly = true)
    public List<ResponsePositionDto> getAll(){
        var allPosition = positionRepository.findAll();
        return positionMapper.toResponseDtoList(allPosition);
    }

    //TODO: add try catch
    public ResponsePositionDto create(PositionDto positionDto){
        if(positionRepository.existsByName(((positionDto.getName())))){
            throw new IllegalArgumentException("Position name already exists");
        }
        var newPosition = positionMapper.toEntity(positionDto);
        positionRepository.save(newPosition);
        return positionMapper.toResponseDto(newPosition);
    }

    //TODO: add try catch
    public ResponsePositionDto update(int positionId, PositionDto positionDto){
        var existingPosition = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect position id"));
        positionMapper.updateEntityFromDto(positionDto, existingPosition);
        positionRepository.save(existingPosition);
        return positionMapper.toResponseDto(existingPosition);
    }

    //TODO: modify later
    public void delete(int positionId){
        var existingPosition = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect position id"));
    }
}