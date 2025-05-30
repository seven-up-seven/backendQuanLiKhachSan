package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RevenueReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RevenueReportRepository extends JpaRepository<RevenueReport, Integer> {
    Optional<RevenueReport> findByMonthAndYear(byte month, short year);

    List<RevenueReport> findRevenueReportsByTotalMonthRevenueGreaterThan(double totalMonthRevenueIsGreaterThan);

    List<RevenueReport> findRevenueReportsByTotalMonthRevenueEquals(double totalMonthRevenueIsEquals);

    List<RevenueReport> findRevenueReportsByTotalMonthRevenueLessThan(double totalMonthRevenueIsLessThan);
}

