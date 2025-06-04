package com.example.backendqlks.service;

import com.example.backendqlks.dao.UserRolePermissionRepository;
import com.example.backendqlks.dto.userrolepermission.ResponseUserRolePermissionDto;
import com.example.backendqlks.dto.userrolepermission.UserRolePermissionDto;
import com.example.backendqlks.entity.UserRolePermission;
import com.example.backendqlks.entity.compositekeys.RolePermissionPrimaryKey;
import com.example.backendqlks.mapper.UserRolePermissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRolePermissionService {
    private final UserRolePermissionRepository rolePermissionRepository;
    private final UserRolePermissionMapper userRolePermissionMapper;

    public UserRolePermissionService(UserRolePermissionRepository rolePermissionRepository,
                                     UserRolePermissionMapper userRolePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRolePermissionMapper = userRolePermissionMapper;
    }

    @Transactional(readOnly = true)
    public List<ResponseUserRolePermissionDto> getAllUserRolePermissions() {
        List<UserRolePermission> permissions = rolePermissionRepository.findAll();
        return userRolePermissionMapper.toResponseDtoList(permissions);
    }

    @Transactional(readOnly = true)
    public ResponseUserRolePermissionDto getUserRolePermissionById(RolePermissionPrimaryKey id) {
        UserRolePermission permission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserRolePermission with this ID cannot be found"));
        return userRolePermissionMapper.toResponseDto(permission);
    }

    public ResponseUserRolePermissionDto createUserRolePermission(UserRolePermissionDto dto) {
        RolePermissionPrimaryKey id = new RolePermissionPrimaryKey(dto.getUserRoleId(), dto.getPermissionId());
        if (rolePermissionRepository.existsById(id)) {
            throw new IllegalArgumentException("UserRolePermission already exists");
        }
        UserRolePermission permission = userRolePermissionMapper.toEntity(dto);
        rolePermissionRepository.save(permission);
        return userRolePermissionMapper.toResponseDto(permission);
    }

    public ResponseUserRolePermissionDto updateUserRolePermission(RolePermissionPrimaryKey id, UserRolePermissionDto dto) {
        UserRolePermission permission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserRolePermission with this ID cannot be found"));
        userRolePermissionMapper.updateEntityFromDto(dto, permission);
        rolePermissionRepository.save(permission);
        return userRolePermissionMapper.toResponseDto(permission);
    }

    public void deleteUserRolePermissionById(RolePermissionPrimaryKey id) {
        UserRolePermission permission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserRolePermission with this ID cannot be found"));
        rolePermissionRepository.delete(permission);
    }
}
