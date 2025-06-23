package com.example.backendqlks.dao;

import com.example.backendqlks.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    List<ImageEntity> findAllByRoom_Id(Integer roomId);
}
