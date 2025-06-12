package com.example.backendqlks.service;

import com.example.backendqlks.dao.UserRolePermissionRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.userrolepermission.ResponseUserRolePermissionDto;
import com.example.backendqlks.dto.userrolepermission.UserRolePermissionDto;
import com.example.backendqlks.entity.UserRolePermission;
import com.example.backendqlks.entity.compositekeys.RolePermissionPrimaryKey;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.UserRolePermissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserRolePermissionService {
    private final UserRolePermissionRepository rolePermissionRepository;
    private final UserRolePermissionMapper userRolePermissionMapper;
    private final UserRolePermissionRepository userRolePermissionRepository;
    private final HistoryService historyService;

    public UserRolePermissionService(UserRolePermissionRepository rolePermissionRepository,
                                     UserRolePermissionMapper userRolePermissionMapper,
                                     UserRolePermissionRepository userRolePermissionRepository,
                                     HistoryService historyService) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRolePermissionMapper = userRolePermissionMapper;
        this.userRolePermissionRepository = userRolePermissionRepository;
        this.historyService = historyService;
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

    @Transactional(readOnly = true)
    public List<ResponseUserRolePermissionDto> getUserRolePermissionsByRoleId(int id) {
        List<UserRolePermission> userRolePermissionList=userRolePermissionRepository.findByUserRoleId(id);
        return userRolePermissionMapper.toResponseDtoList(userRolePermissionList);
    }

    public ResponseUserRolePermissionDto createUserRolePermission(UserRolePermissionDto dto, int impactorId, String impactor) {
        RolePermissionPrimaryKey id = new RolePermissionPrimaryKey(dto.getUserRoleId(), dto.getPermissionId());
        if (rolePermissionRepository.existsById(id)) {
            throw new IllegalArgumentException("UserRolePermission already exists");
        }
        UserRolePermission permission = userRolePermissionMapper.toEntity(dto);
        rolePermissionRepository.save(permission);
        String content = String.format(
                "Phân quyền: Vai trò ID: %d được cấp quyền ID: %d",
                permission.getUserRoleId(), permission.getPermissionId()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phân quyền vai trò")
                .affectedObjectId(permission.getUserRoleId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return userRolePermissionMapper.toResponseDto(permission);
    }

    public ResponseUserRolePermissionDto updateUserRolePermission(RolePermissionPrimaryKey id, UserRolePermissionDto dto, int impactorId, String impactor) {
        UserRolePermission permission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserRolePermission with this ID cannot be found"));
        userRolePermissionMapper.updateEntityFromDto(dto, permission);
        rolePermissionRepository.save(permission);
        return userRolePermissionMapper.toResponseDto(permission);
    }

    public void deleteUserRolePermissionById(RolePermissionPrimaryKey id, int impactorId, String impactor) {
        UserRolePermission permission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserRolePermission with this ID cannot be found"));
        String content = String.format(
                "Thu hồi quyền: Gỡ quyền ID: %d khỏi vai trò ID: %d",
                permission.getPermissionId(), permission.getUserRoleId()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phân quyền vai trò")
                .affectedObjectId(permission.getUserRoleId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        rolePermissionRepository.delete(permission);
    }
}
