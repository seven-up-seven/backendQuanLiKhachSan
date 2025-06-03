package com.example.backendqlks.service;

import com.example.backendqlks.dao.UserRoleRepository;
import com.example.backendqlks.dto.userrole.ResponseUserRoleDto;
import com.example.backendqlks.dto.userrole.UserRoleDto;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleService(UserRoleMapper userRoleMapper, UserRoleRepository userRoleRepository) {
        this.userRoleMapper = userRoleMapper;
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseUserRoleDto> getAllUserRoles() {
        List<UserRole> userRoles = userRoleRepository.findAll();
        return userRoleMapper.toResponseDtoList(userRoles);
    }

    @Transactional(readOnly = true)
    public ResponseUserRoleDto getUserRoleById(int id) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Role with this ID cannot be found"));
        return userRoleMapper.toResponseDto(userRole);
    }

    public ResponseUserRoleDto createUserRole(UserRoleDto userRoleDto) {
        UserRole userRole = userRoleMapper.toEntity(userRoleDto);
        userRoleRepository.save(userRole);
        return userRoleMapper.toResponseDto(userRole);
    }

    public ResponseUserRoleDto updateUserRole(int id, UserRoleDto userRoleDto) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Role with this ID cannot be found"));
        userRoleMapper.updateEntityFromDto(userRoleDto, userRole);
        userRoleRepository.save(userRole);
        return userRoleMapper.toResponseDto(userRole);
    }

    public void deleteUserRoleById(int id) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Role with this ID cannot be found"));
        //considering checking foreign key relation before deleting
        //userRoleRepository.delete(userRole);
    }
}
