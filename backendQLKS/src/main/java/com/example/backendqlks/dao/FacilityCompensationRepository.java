package com.example.backendqlks.dao;

import com.example.backendqlks.entity.FacilityCompensation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface FacilityCompensationRepository extends JpaRepository<FacilityCompensation, Integer> {
    Page<FacilityCompensation> findFacilityCompensationsByFacilityId(int facilityId, Pageable pageable);

    List<FacilityCompensation> findFacilityCompensationsByInvoiceDetailId(int invoiceDetailId);
}
