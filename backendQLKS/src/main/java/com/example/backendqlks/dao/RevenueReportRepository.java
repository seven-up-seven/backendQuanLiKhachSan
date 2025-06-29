package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RevenueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RevenueReportRepository extends JpaRepository<RevenueReport, Integer> {
    Optional<RevenueReport> findByMonthAndYear(byte month, short year);

    List<RevenueReport> findRevenueReportsByTotalMonthRevenueGreaterThan(double totalMonthRevenueIsGreaterThan);

    List<RevenueReport> findRevenueReportsByTotalMonthRevenueEquals(double totalMonthRevenueIsEquals);

    List<RevenueReport> findRevenueReportsByTotalMonthRevenueLessThan(double totalMonthRevenueIsLessThan);

    List<RevenueReport> findByYear(short year);

    boolean existsByMonthAndYear(byte month, short year);
}

