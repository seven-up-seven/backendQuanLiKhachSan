package com.example.backendqlks.service;

import com.example.backendqlks.dao.VariableRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.variable.ResponseVariableDto;
import com.example.backendqlks.dto.variable.VariableDto;
import com.example.backendqlks.entity.Variable;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.VariableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class VariableService {
    private final VariableRepository variableRepository;
    private final VariableMapper variableMapper;
    private final HistoryService historyService;

    public VariableService(VariableRepository variableRepository,
                           VariableMapper variableMapper,
                           HistoryService historyService) {
        this.variableRepository = variableRepository;
        this.variableMapper = variableMapper;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public List<ResponseVariableDto> getAll() {
        return variableMapper.toResponseDtoList(variableRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ResponseVariableDto getById(int id) {
        Variable variable = variableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variable with this ID cannot be found"));
        return variableMapper.toResponseDto(variable);
    }

    public ResponseVariableDto create(VariableDto variableDto) {
        if (variableRepository.findByNameEqualsIgnoreCase(variableDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Variable with this name already exists");
        }
        var v = variableMapper.toEntity(variableDto);
        Variable savedVariable = variableRepository.save(v);
        return variableMapper.toResponseDto(savedVariable);
    }

    public ResponseVariableDto update(int id, VariableDto variableDto, int impactorId, String impactor) {
        Variable variable = variableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variable with this ID cannot be found"));

        // Lưu thông tin cũ
        Variable oldVariable = variableRepository.findById(id).get();

        StringBuilder contentBuilder = new StringBuilder();

        if (!Objects.equals(oldVariable.getValue(), variableDto.getValue())) {
            contentBuilder.append(String.format("Giá trị: %s -> %s; ", oldVariable.getValue(), variableDto.getValue()));
        }

        if (!Objects.equals(oldVariable.getDescription(), variableDto.getDescription())) {
            contentBuilder.append(String.format("Mô tả: %s -> %s; ",
                    oldVariable.getDescription() == null ? "null" : oldVariable.getDescription(),
                    variableDto.getDescription() == null ? "null" : variableDto.getDescription()));
        }

        // Nếu có thay đổi thì ghi lại lịch sử
        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Biến hệ thống")
                    .affectedObjectId(variable.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }

        // Cập nhật và lưu lại
        variableMapper.updateEntityFromDto(variableDto, variable);
        Variable updated = variableRepository.save(variable);

        return variableMapper.toResponseDto(updated);
    }

    public void delete(int id) {
        Variable variable = variableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variable with this ID cannot be found"));
        variableRepository.delete(variable);
    }

    @Transactional(readOnly = true)
    public ResponseVariableDto getByName(String name) {
        Variable variable = variableRepository.findByNameEqualsIgnoreCase(name)
                .orElseThrow(() -> new IllegalArgumentException("Variable with this name cannot be found"));
        return variableMapper.toResponseDto(variable);
    }
}
