package com.example.backendqlks.service;

import com.example.backendqlks.dao.PermissionRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.permission.PermissionDto;
import com.example.backendqlks.dto.permission.ResponsePermissionDto;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.PermissionMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@Validated
@Transactional
@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final HistoryService historyService;

    public PermissionService(PermissionRepository permissionRepository,
                             PermissionMapper permissionMapper,
                             HistoryService historyService) {
        this.permissionMapper = permissionMapper;
        this.permissionRepository = permissionRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public ResponsePermissionDto get(int permissionId){
        var existingPermission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect permission id"));
        return permissionMapper.toResponseDto(existingPermission);
    }

    @Transactional(readOnly = true)
    public List<ResponsePermissionDto> getAll(){
        var allPermission = permissionRepository.findAll();
        return permissionMapper.toResponseDtoList(allPermission);
    }

    //TODO: add try catch
    public ResponsePermissionDto create(PermissionDto permissionDto, int impactorId, String impactor){
        if(permissionRepository.existsByName(((permissionDto.getName())))){
            throw new IllegalArgumentException("Permission name already exists");
        }
        var newPermission = permissionMapper.toEntity(permissionDto);
        permissionRepository.save(newPermission);
        String content = String.format("Tên quyền: %s", permissionDto.getName());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Quyền")
                .affectedObjectId(newPermission.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return permissionMapper.toResponseDto(newPermission);
    }

    //TODO: add try catch
    public ResponsePermissionDto update(int permissionId, PermissionDto permissionDto, int impactorId, String impactor){
        var existingPermission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect permission id"));
        permissionMapper.updateEntityFromDto(permissionDto, existingPermission);
        permissionRepository.save(existingPermission);
        StringBuilder contentBuilder = new StringBuilder();
        if (!Objects.equals(existingPermission.getName(), permissionDto.getName())) {
            contentBuilder.append(String.format("Tên quyền: %s -> %s", existingPermission.getName(), permissionDto.getName()));
        }
        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Quyền")
                    .affectedObjectId(existingPermission.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }
        return permissionMapper.toResponseDto(existingPermission);
    }

    //TODO: modify later
    public void delete(int permissionId, int impactorId, String impactor){
        var existingPermission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect permission id"));
        String content = String.format("Tên quyền: %s", existingPermission.getName());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Quyền")
                .affectedObjectId(existingPermission.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        permissionRepository.delete(existingPermission);
    }
}