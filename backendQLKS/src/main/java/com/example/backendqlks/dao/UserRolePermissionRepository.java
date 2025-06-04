package com.example.backendqlks.dao;

import com.example.backendqlks.entity.UserRolePermission;
import com.example.backendqlks.entity.compositekeys.RolePermissionPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, RolePermissionPrimaryKey> {
    List<UserRolePermission> findByUserRoleId(int userRoleId);

    List<UserRolePermission> findByPermissionId(int permissionId);
}
