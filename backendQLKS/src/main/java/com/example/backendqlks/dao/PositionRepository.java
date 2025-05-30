package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    List<Position> findByBaseSalary(double baseSalary);
}
