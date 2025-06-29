package com.example.backendqlks.service;

import com.example.backendqlks.dao.HistoryRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.history.ResponseHistoryDto;
import com.example.backendqlks.entity.History;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.HistoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        return historyMapper.toResponseDto(historyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incorrect fucking history id!")));
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

    @Transactional(readOnly = true)
    public List<ResponseHistoryDto> getByExecuteAtBetween(LocalDateTime start, LocalDateTime end) {
        return historyMapper.toResponseDtoList(historyRepository.findByExecuteAtBetween(start, end));
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

    @Transactional
    public void saveHistory(String impactor, String affectedObject, int impactorId, int affectedObjectId, String action, String content) {
        // Ánh xạ String sang enum Action
        Action actionEnum;
        try {
            actionEnum = Action.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Hành động không hợp lệ: " + action);
        }

        // Sử dụng builder pattern để tạo HistoryDto
        HistoryDto historyDto = HistoryDto.builder()
                .impactor(impactor)
                .affectedObject(affectedObject)
                .impactorId(impactorId)
                .affectedObjectId(affectedObjectId)
                .action(actionEnum)
                .content(content)
                .build();

        create(historyDto);
    }
}