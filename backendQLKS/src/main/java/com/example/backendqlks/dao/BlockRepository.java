package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface BlockRepository extends JpaRepository<Block, Integer> {
    List<Block> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
