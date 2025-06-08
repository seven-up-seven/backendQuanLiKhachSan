package com.example.backendqlks.dao;

import com.example.backendqlks.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    Optional<UserRole> findByNameEqualsIgnoreCase(String name);
}
