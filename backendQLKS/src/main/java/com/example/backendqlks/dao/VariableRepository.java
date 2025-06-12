package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Variable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariableRepository extends JpaRepository<Variable, Integer> {
}
