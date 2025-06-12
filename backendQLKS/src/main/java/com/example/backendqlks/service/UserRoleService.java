package com.example.backendqlks.service;

import com.example.backendqlks.dao.UserRoleRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.userrole.ResponseUserRoleDto;
import com.example.backendqlks.dto.userrole.UserRoleDto;
import com.example.backendqlks.entity.UserRole;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;
    private final HistoryService historyService;

    public UserRoleService(UserRoleMapper userRoleMapper, UserRoleRepository userRoleRepository, HistoryService historyService) {
        this.userRoleMapper = userRoleMapper;
        this.userRoleRepository = userRoleRepository;
        this.historyService = historyService;
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

    //kiểm tra xem có user role nào có tên giống với tên trong userRoleDto không, sau đó mới cho tạo user role
    public ResponseUserRoleDto createUserRole(UserRoleDto userRoleDto, int impactorId, String impactor) {
        if (userRoleRepository.findByNameEqualsIgnoreCase(userRoleDto.getName()).isPresent()) {
            throw new IllegalArgumentException("User Role with this name already exists");
        }
        UserRole userRole = userRoleMapper.toEntity(userRoleDto);
        userRoleRepository.save(userRole);
        String content = String.format("Tạo vai trò mới: %s", userRole.getName());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Vai trò người dùng")
                .affectedObjectId(userRole.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return userRoleMapper.toResponseDto(userRole);
    }

    public ResponseUserRoleDto updateUserRole(int id, UserRoleDto userRoleDto, int impactorId, String impactor) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Role with this ID cannot be found"));
        String oldName = userRole.getName();
        userRoleMapper.updateEntityFromDto(userRoleDto, userRole);
        if (!Objects.equals(oldName, userRole.getName())) {
            String content = String.format("Tên: %s -> %s", oldName, userRole.getName());
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Vai trò người dùng")
                    .affectedObjectId(userRole.getId())
                    .action(Action.UPDATE)
                    .content(content)
                    .build();
            historyService.create(history);
        }
        userRoleRepository.save(userRole);
        return userRoleMapper.toResponseDto(userRole);
    }

    public void deleteUserRoleById(int id, int impactorId, String impactor) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Role with this ID cannot be found"));
        String content = String.format("Xóa vai trò: %s (ID: %d)", userRole.getName(), userRole.getId());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Vai trò người dùng")
                .affectedObjectId(userRole.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        userRoleRepository.delete(userRole);
    }
}
