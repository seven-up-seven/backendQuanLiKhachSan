package com.example.backendqlks.dao;

import com.example.backendqlks.entity.UserRolePermission;
import com.example.backendqlks.entity.compositekeys.RolePermissionPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, RolePermissionPrimaryKey> {
    List<UserRolePermission> findByUserRoleId(int userRoleId);

    List<UserRolePermission> findByPermissionId(int permissionId);
}
