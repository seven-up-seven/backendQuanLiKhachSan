package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Page<Invoice> findAllByStaff(Staff staff, Pageable pageable);

    Page<Invoice> findAllByPayingGuest(Guest guest, Pageable pageable);

    List<Invoice> findInvoicesByPayingGuestId(int payingGuestId);

    List<Invoice> findInvoiceByCreatedAtBetween(Date startDate, Date endDate);
}
