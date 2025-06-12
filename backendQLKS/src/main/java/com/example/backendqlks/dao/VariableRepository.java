package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Variable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.swing.text.html.Option;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface VariableRepository extends JpaRepository<Variable, Integer> {
    Optional<Variable> findByName(String name);

    Optional<Variable> findByNameEqualsIgnoreCase(String name);
}
