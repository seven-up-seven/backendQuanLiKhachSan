package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface GuestRepository extends JpaRepository<Guest, Integer> {
    List<Guest> findGuestsByNameContainingIgnoreCase(String name);

    Optional<Guest> findByPhoneNumber(String phoneNumber);

    Optional<Guest> findByIdentificationNumber(String identificationNumber);

    Optional<Guest> findByEmailContainingIgnoreCase(String email);

    Optional<Guest> findByAccountId(Integer accountId);
}
