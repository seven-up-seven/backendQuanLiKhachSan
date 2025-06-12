package com.example.backendqlks.service;

import com.example.backendqlks.dao.HistoryRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.history.ResponseHistoryDto;
import com.example.backendqlks.entity.History;
import com.example.backendqlks.mapper.HistoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class HistoryService {
    private final HistoryMapper historyMapper;
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryMapper historyMapper, HistoryRepository historyRepository) {
        this.historyMapper = historyMapper;
        this.historyRepository = historyRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseHistoryDto> getAll() {
        List<History> historyList = historyRepository.findAll();
        return historyMapper.toResponseDtoList(historyList);
    }

    @Transactional(readOnly = true)
    public ResponseHistoryDto getById(int id) {
        return historyMapper.toResponseDto(historyRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Incorrect fucking history id!")));
    }

    @Transactional(readOnly = true)
    public List<ResponseHistoryDto> getByImpactor(String impactor) {
        List<History> histories = historyRepository.findByImpactor(impactor);
        return historyMapper.toResponseDtoList(histories);
    }

    @Transactional(readOnly = true)
    public List<ResponseHistoryDto> getByAffectedObject(String affectedObject) {
        List<History> histories = historyRepository.findByAffectedObject(affectedObject);
        return historyMapper.toResponseDtoList(histories);
    }

    @Transactional(readOnly = true)
    public List<ResponseHistoryDto> getByImpactorId(int impactorId) {
        return historyMapper.toResponseDtoList(historyRepository.findByImpactorId(impactorId));
    }

    @Transactional(readOnly = true)
    public List<ResponseHistoryDto> getByAffectedObjectId(int affectedObjectId) {
        return historyMapper.toResponseDtoList(historyRepository.findByAffectedObjectId(affectedObjectId));
    }

    public ResponseHistoryDto create(HistoryDto historyDto) {
        try {
            History history = historyMapper.toEntity(historyDto);
            historyRepository.save(history);
            return historyMapper.toResponseDto(history);
        } catch (Exception e) {
            throw new RuntimeException("Error creating history record: " + e.getMessage(), e);
        }
    }
}
