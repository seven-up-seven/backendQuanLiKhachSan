package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Facility;
import com.example.backendqlks.entity.ImportInvoice;
import com.example.backendqlks.entity.ImportInvoiceDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportInvoiceDetailRepository extends JpaRepository<ImportInvoiceDetail, Integer> {
    Page<ImportInvoiceDetail> findAll(Pageable pageable);

    Page<ImportInvoiceDetail> findAllByFacility(Facility facility, Pageable pageable);

    Page<ImportInvoiceDetail> findAllByImportInvoice(ImportInvoice importInvoice, Pageable pageable);
}
