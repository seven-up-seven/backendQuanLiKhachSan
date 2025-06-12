package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsernameContainingIgnoreCase(String username);

    Page<Account> findAccountsByUserRoleId(int userRoleId, Pageable pageable);

    boolean existsByUsername(String username);

    Optional<Account> findByUsername(String username);
}
