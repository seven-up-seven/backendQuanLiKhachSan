package com.example.backendqlks.dao;

import com.example.backendqlks.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {
    List<InvoiceDetail> findAllByInvoiceId(Integer invoiceId);

    Optional<InvoiceDetail> findByRentalFormId(Integer rentalFormId);
}
