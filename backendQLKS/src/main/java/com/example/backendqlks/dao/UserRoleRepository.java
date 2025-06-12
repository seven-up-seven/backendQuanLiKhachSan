package com.example.backendqlks.dao;

import com.example.backendqlks.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByNameEqualsIgnoreCase(String name);
}
