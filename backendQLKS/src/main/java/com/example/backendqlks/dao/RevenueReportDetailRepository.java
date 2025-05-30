package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RevenueReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevenueReportDetailRepository extends JpaRepository<RevenueReportDetail, Integer> {
    List<RevenueReportDetail> findRevenueReportDetailsByRoomTypeId(int roomTypeId);
}
