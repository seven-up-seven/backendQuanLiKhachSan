package com.example.backendqlks.dao;

import com.example.backendqlks.entity.ImportInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.Optional;

public interface ImportInvoiceRepository extends JpaRepository<ImportInvoice, Integer> {
    @Query("select ii from ImportInvoice ii inner join ii.importInvoiceDetails details where details.id=:importInvoiceDetailId")
    Optional<ImportInvoice> findByImportInvoiceDetailId(@Param("importInvoiceDetailId") Integer importInvoiceDetailId);

    Page<ImportInvoice> findAll(Pageable pageable);
}
