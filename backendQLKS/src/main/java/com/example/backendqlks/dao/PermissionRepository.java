package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByNameContainingIgnoreCase(String name);
}
