package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Page<Invoice> findAll(Pageable pageable);

    Page<Invoice> findAllByStaff(Staff staff, Pageable pageable);

    Page<Invoice> findAllByPayingGuest(Guest guest, Pageable pageable);
}
