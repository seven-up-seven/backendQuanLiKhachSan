package com.example.backendqlks.service;

import com.example.backendqlks.dao.PermissionRepository;
import com.example.backendqlks.dto.permission.PermissionDto;
import com.example.backendqlks.dto.permission.ResponsePermissionDto;
import com.example.backendqlks.mapper.PermissionMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Transactional
@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository,
                             PermissionMapper permissionMapper){
        this.permissionMapper = permissionMapper;
        this.permissionRepository = permissionRepository;
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
    public ResponsePermissionDto create(PermissionDto permissionDto){
        if(permissionRepository.existsByName(((permissionDto.getName())))){
            throw new IllegalArgumentException("Permission name already exists");
        }
        var newPermission = permissionMapper.toEntity(permissionDto);
        permissionRepository.save(newPermission);
        return permissionMapper.toResponseDto(newPermission);
    }

    //TODO: add try catch
    public ResponsePermissionDto update(int permissionId, PermissionDto permissionDto){
        var existingPermission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect permission id"));
        permissionMapper.updateEntityFromDto(permissionDto, existingPermission);
        permissionRepository.save(existingPermission);
        return permissionMapper.toResponseDto(existingPermission);
    }

    //TODO: modify later
    public void delete(int permissionId){
        var existingPermission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect permission id"));
    }
}