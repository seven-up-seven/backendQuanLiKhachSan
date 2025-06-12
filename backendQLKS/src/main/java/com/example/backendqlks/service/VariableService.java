package com.example.backendqlks.service;

import com.example.backendqlks.dao.VariableRepository;
import com.example.backendqlks.dto.variable.ResponseVariableDto;
import com.example.backendqlks.dto.variable.VariableDto;
import com.example.backendqlks.entity.Variable;
import com.example.backendqlks.mapper.VariableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class VariableService {
    private final VariableRepository variableRepository;
    private final VariableMapper variableMapper;

    public VariableService(VariableRepository variableRepository, VariableMapper variableMapper) {
        this.variableRepository = variableRepository;
        this.variableMapper = variableMapper;
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

    public ResponseVariableDto update(int id, VariableDto variableDto) {
        Variable existingVariable = variableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variable with this ID cannot be found"));
        variableMapper.updateEntityFromDto(variableDto, existingVariable);
        Variable updatedVariable = variableRepository.save(existingVariable);
        return variableMapper.toResponseDto(updatedVariable);
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
