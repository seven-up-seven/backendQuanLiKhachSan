package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RentalFormDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalFormDetailRepository extends JpaRepository<RentalFormDetail, Integer> {
    Optional<RentalFormDetail> findByRentalFormIdAndGuestId(int rentalFormId, int guestId);
    boolean existsByRentalFormIdAndGuestId(int rentalFormId, int guestId);
}
