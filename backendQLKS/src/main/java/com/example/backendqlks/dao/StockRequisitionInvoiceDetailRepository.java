package com.example.backendqlks.dao;

import com.example.backendqlks.entity.StockRequisitionInvoiceDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRequisitionInvoiceDetailRepository extends JpaRepository<StockRequisitionInvoiceDetail, Integer> {
    Page<StockRequisitionInvoiceDetail> findByTotalQuantity(short quantity, Pageable pageable);

    Page<StockRequisitionInvoiceDetail> findByTotalQuantityGreaterThan(short quantity, Pageable pageable);

    Page<StockRequisitionInvoiceDetail> findByTotalQuantityLessThan(short quantity, Pageable pageable);

    Page<StockRequisitionInvoiceDetail> findByFacilityId(int facilityId, Pageable pageable);

    Page<StockRequisitionInvoiceDetail> findByStockRequisitionInvoiceId(int stockRequisitionInvoiceId, Pageable pageable);
}
