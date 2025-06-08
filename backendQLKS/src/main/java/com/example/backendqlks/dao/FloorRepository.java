package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface FloorRepository extends JpaRepository<Floor, Integer> {
    Optional<Floor> findByNameContainingIgnoreCase(String name);

    Optional<Floor> findByNameAndBlockName(String name, String blockName);

    Optional<Floor> findByNameAndBlockId(String name, int blockId);

    // TODO: test this as a query : List<Floor> findFloorsByBlockName(String blockName);
}
