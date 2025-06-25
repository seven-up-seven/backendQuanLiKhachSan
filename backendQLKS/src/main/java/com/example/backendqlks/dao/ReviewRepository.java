package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByRoom_Id(Integer roomId);
    List<Review> findAllByGuest_Id(Integer guestId);
}