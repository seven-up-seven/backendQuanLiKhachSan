package com.example.backendqlks.dao;

import com.example.backendqlks.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByNameIgnoreCase(String name);
}
