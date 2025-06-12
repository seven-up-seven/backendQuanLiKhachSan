package com.example.backendqlks.dao;

import com.example.backendqlks.entity.InvoiceDetail;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {
    List<InvoiceDetail> findAllByInvoiceId(Integer invoiceId);

    Optional<InvoiceDetail> findByRentalFormId(Integer rentalFormId);
}
