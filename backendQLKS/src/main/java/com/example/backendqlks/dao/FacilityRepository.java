package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    Optional<Facility> findByNameContainingIgnoreCase(String name);
}
