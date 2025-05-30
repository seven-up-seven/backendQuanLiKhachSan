package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.entity.RentalForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentalExtensionFormRepository extends JpaRepository<RentalExtensionForm, Integer> {
    Page<RentalExtensionForm> findAll(Pageable pageable);

    List<RentalExtensionForm> findByRentalFormId(int rentalFormId);
}
