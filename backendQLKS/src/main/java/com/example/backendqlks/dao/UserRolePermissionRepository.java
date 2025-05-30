package com.example.backendqlks.dao;

import com.example.backendqlks.entity.UserRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Long> {
    List<UserRolePermission> findByUserRoleId(int id);

    List<UserRolePermission> findByPermissionId(int id);
}
