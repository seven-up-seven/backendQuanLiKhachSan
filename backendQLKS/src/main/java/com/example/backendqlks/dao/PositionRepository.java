package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface PositionRepository extends JpaRepository<Position, Integer> {
    List<Position> findByBaseSalary(double baseSalary);

    boolean existsByName(String name);
}
