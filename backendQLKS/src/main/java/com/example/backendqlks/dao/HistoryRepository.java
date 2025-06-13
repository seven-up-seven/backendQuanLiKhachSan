package com.example.backendqlks.dao;

import com.example.backendqlks.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> findByImpactor(String impactor);

    List<History> findByAffectedObject(String affectedObject);

    List<History> findByImpactorId(Integer impactorId);

    List<History> findByAffectedObjectId(Integer affectedObjectId);

    List<History> findByExecuteAtBetween(LocalDateTime start, LocalDateTime end);
}
