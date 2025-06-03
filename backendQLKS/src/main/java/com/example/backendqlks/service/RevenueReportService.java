package com.example.backendqlks.service;

import com.example.backendqlks.dao.RevenueReportRepository;
import com.example.backendqlks.dto.revenuereport.ResponseRevenueReportDto;
import com.example.backendqlks.dto.revenuereport.RevenueReportDto;
import com.example.backendqlks.entity.RevenueReport;
import com.example.backendqlks.mapper.RevenueReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RevenueReportService {
    private final RevenueReportRepository revenueReportRepository;
    private final RevenueReportMapper revenueReportMapper;

    public RevenueReportService(RevenueReportMapper revenueReportMapper,
                                RevenueReportRepository revenueReportRepository) {
        this.revenueReportMapper = revenueReportMapper;
        this.revenueReportRepository = revenueReportRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseRevenueReportDto> getAllRevenueReports() {
        List<RevenueReport> revenueReports = revenueReportRepository.findAll();
        return revenueReportMapper.toResponseDtoList(revenueReports);
    }

    @Transactional(readOnly = true)
    public ResponseRevenueReportDto getRevenueReportById(int id) {
        RevenueReport revenueReport = revenueReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report with this ID cannot be found"));
        return revenueReportMapper.toResponseDto(revenueReport);
    }

    public ResponseRevenueReportDto createRevenueReport(RevenueReportDto revenueReportDto) {
        RevenueReport revenueReport = revenueReportMapper.toEntity(revenueReportDto);
        revenueReportRepository.save(revenueReport);
        return revenueReportMapper.toResponseDto(revenueReport);
    }

    public ResponseRevenueReportDto updateRevenueReport(int id, RevenueReportDto revenueReportDto) {
        RevenueReport revenueReport = revenueReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report with this ID cannot be found"));
        revenueReportMapper.updateEntityFromDto(revenueReportDto, revenueReport);
        revenueReportRepository.save(revenueReport);
        return revenueReportMapper.toResponseDto(revenueReport);
    }

    public void deleteRevenueReportById(int id) {
        RevenueReport revenueReport = revenueReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report with this ID cannot be found"));
        revenueReportRepository.delete(revenueReport);
    }
}
