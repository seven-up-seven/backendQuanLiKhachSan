package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
    List<Guest> findGuestsByNameContainingIgnoreCase(String name);

    Optional<Guest> findByPhoneNumber(String phoneNumber);

    Optional<Guest> findByIdentificationNumber(String identificationNumber);

    Optional<Guest> findByEmailContainingIgnoreCase(String email);

    Optional<Guest> findByAccountId(Integer accountId);
}
