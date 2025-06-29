package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RentalFormDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RentalFormDetailRepository extends JpaRepository<RentalFormDetail, Integer> {
    Optional<RentalFormDetail> findByRentalFormIdAndGuestId(int rentalFormId, int guestId);
    boolean existsByRentalFormIdAndGuestId(int rentalFormId, int guestId);
    List<RentalFormDetail> findRentalFormDetailsByGuestId(int guestId);
    List<RentalFormDetail> findRentalFormDetailsByRentalFormId(int rentalFormId);
}
