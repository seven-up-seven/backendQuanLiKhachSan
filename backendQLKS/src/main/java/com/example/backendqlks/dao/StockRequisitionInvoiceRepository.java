package com.example.backendqlks.dao;

import com.example.backendqlks.entity.StockRequisitionInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRequisitionInvoiceRepository extends JpaRepository<StockRequisitionInvoice, Integer> {
    Page<StockRequisitionInvoice> findByQuantity(short quantity, Pageable pageable);

    Page<StockRequisitionInvoice> findByQuantityGreaterThan(short quantity, Pageable pageable);

    Page<StockRequisitionInvoice> findByQuantityLessThan(short quantity, Pageable pageable);

    Page<StockRequisitionInvoice> findByRoomId(int roomId, Pageable pageable);
}
