package com.example.backendqlks.service;

import com.example.backendqlks.dao.BlockRepository;
import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.dto.account.ResponseAccountDto;
import com.example.backendqlks.dto.block.BlockDto;
import com.example.backendqlks.dto.block.ResponseBlockDto;
import com.example.backendqlks.mapper.BlockMapper;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Transactional
@Service
public class BlockService {
    private final BlockRepository blockRepository;
    private final BlockMapper blockMapper;

    public BlockService(BlockRepository blockRepository,
                        BlockMapper blockMapper){
        this.blockMapper = blockMapper;
        this.blockRepository = blockRepository;
    }

    @Transactional(readOnly = true)
    public ResponseBlockDto get(int blockId){
        var existingBlock = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect block id"));
        return blockMapper.toResponseDto(existingBlock);
    }

    @Transactional(readOnly = true)
    public List<ResponseBlockDto> getAll(){
        var allBlock = blockRepository.findAll();
        return blockMapper.toResponseDtoList(allBlock);
    }

    //TODO: add try catch
    public ResponseBlockDto create(BlockDto blockDto){
        if(blockRepository.existsByName((blockDto.getName()))){
            throw new IllegalArgumentException("Block name already exists");
        }
        var newBlock = blockMapper.toEntity(blockDto);
        blockRepository.save(newBlock);
        return blockMapper.toResponseDto(newBlock);
    }

    //TODO: add try catch
    public ResponseBlockDto update(int blockId, BlockDto blockDto){
        var existingBlock = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect account id"));
        blockMapper.updateEntityFromDto(blockDto, existingBlock);
        blockRepository.save(existingBlock);
        return blockMapper.toResponseDto(existingBlock);
    }
    //TODO: modify later
    public void delete(int blockId){
        var existingBlock = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect account id"));
        blockRepository.delete(existingBlock);
    }
}

