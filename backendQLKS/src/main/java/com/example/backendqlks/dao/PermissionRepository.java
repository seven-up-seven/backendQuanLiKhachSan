package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
