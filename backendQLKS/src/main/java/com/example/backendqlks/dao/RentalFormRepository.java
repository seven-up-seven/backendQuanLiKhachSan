package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.RentalFormDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RentalFormRepository extends JpaRepository<RentalForm, Integer> {
    Page<RentalForm> findAll(Pageable pageable);

    Page<RentalForm> findByRentalDate(LocalDateTime rentalDate, Pageable pageable);

    Optional<RentalForm> findByInvoiceDetailId(Integer invoiceDetailId);
}
