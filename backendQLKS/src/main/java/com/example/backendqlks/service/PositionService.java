package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.PositionRepository;
import com.example.backendqlks.dto.position.PositionDto;
import com.example.backendqlks.dto.position.ResponsePositionDto;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.mapper.PositionMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class PositionService {
    private final PositionRepository positionRepository;
    private final AccountRepository accountRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository,
                           PositionMapper positionMapper,
                           AccountRepository accountRepository) {
        this.positionMapper = positionMapper;
        this.positionRepository = positionRepository;
        this.accountRepository = accountRepository;
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

    // xoá position sẽ xoá luôn các staff có account
    public void delete(int positionId){
        var existingPosition = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect position id"));
        var staffsAccountIds = existingPosition.getStaffs()
                .stream()
                .map(Staff::getAccountId)
                .filter(Objects::nonNull)
                .toList();
        staffsAccountIds.forEach(id-> {
            var account = accountRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account of a staff in list staff of Position entity not found"));
            accountRepository.save(account);
        });
    }
}