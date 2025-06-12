package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RevenueReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface RevenueReportDetailRepository extends JpaRepository<RevenueReportDetail, Integer> {
    List<RevenueReportDetail> findRevenueReportDetailsByRoomTypeId(int roomTypeId);
}
