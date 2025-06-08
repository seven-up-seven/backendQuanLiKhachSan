package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    List<RoomType> findRoomTypesByPriceGreaterThan(double priceIsGreaterThan);

    List<RoomType> findRoomTypesByPriceLessThan(double priceIsLessThan);
    
    Optional<RoomType> findByPriceEquals(double price);

    Optional<RoomType> findByNameContainingIgnoreCase(String name);
}
