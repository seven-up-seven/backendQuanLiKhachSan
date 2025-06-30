package com.example.backendqlks.service;

import com.example.backendqlks.dao.BlockRepository;
import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.dto.account.ResponseAccountDto;
import com.example.backendqlks.dto.block.BlockDto;
import com.example.backendqlks.dto.block.ResponseBlockDto;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.entity.enums.Action;
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
    private final HistoryService historyService;

    public BlockService(BlockRepository blockRepository,
                        BlockMapper blockMapper,
                        HistoryService historyService) {
        this.blockMapper = blockMapper;
        this.blockRepository = blockRepository;
        this.historyService = historyService;
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
    public ResponseBlockDto create(BlockDto blockDto, int impactorId, String impactor){
        if(blockRepository.existsByName((blockDto.getName()))){
            throw new IllegalArgumentException("Block name already exists");
        }
        var newBlock = blockMapper.toEntity(blockDto);
        blockRepository.save(newBlock);
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tòa")
                .affectedObjectId(newBlock.getId())
                .action(Action.CREATE)
                .content("Tên: "+newBlock.getName())
                .build();
        historyService.create(history);
        return blockMapper.toResponseDto(newBlock);
    }

    //TODO: add try catch
    public ResponseBlockDto update(int blockId, BlockDto blockDto, int impactorId, String impactor){
        var existingBlock = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect block id"));
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tòa")
                .affectedObjectId(existingBlock.getId())
                .action(Action.UPDATE)
                .content("Tên: "+existingBlock.getName()+" -> "+blockDto.getName())
                .build();
        historyService.create(history);
        blockMapper.updateEntityFromDto(blockDto, existingBlock);
        blockRepository.save(existingBlock);
        return blockMapper.toResponseDto(existingBlock);
    }
    //TODO: modify later
    public void delete(int blockId, int impactorId, String impactor){
        var existingBlock = blockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect block id"));
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tòa")
                .affectedObjectId(existingBlock.getId())
                .action(Action.DELETE)
                .content("Tên: "+existingBlock.getName())
                .build();
        historyService.create(history);
        blockRepository.delete(existingBlock);
    }
}
