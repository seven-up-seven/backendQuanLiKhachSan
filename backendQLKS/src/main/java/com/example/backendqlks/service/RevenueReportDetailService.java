package com.example.backendqlks.service;

import com.example.backendqlks.dao.RevenueReportDetailRepository;
import com.example.backendqlks.dto.revenuereportdetail.ResponseRevenueReportDetailDto;
import com.example.backendqlks.dto.revenuereportdetail.RevenueReportDetailDto;
import com.example.backendqlks.entity.RevenueReport;
import com.example.backendqlks.entity.RevenueReportDetail;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.mapper.RevenueReportDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RevenueReportDetailService {
    private final RevenueReportDetailRepository revenueReportDetailRepository;
    private final RevenueReportDetailMapper revenueReportDetailMapper;

    public RevenueReportDetailService(RevenueReportDetailMapper revenueReportDetailMapper,
                                      RevenueReportDetailRepository revenueReportDetailRepository) {
        this.revenueReportDetailMapper = revenueReportDetailMapper;
        this.revenueReportDetailRepository = revenueReportDetailRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseRevenueReportDetailDto> getAllRevenueReportDetails() {
        List<RevenueReportDetail> reportDetails = revenueReportDetailRepository.findAll();
        return revenueReportDetailMapper.toResponseDtoList(reportDetails);
    }

    @Transactional(readOnly = true)
    public ResponseRevenueReportDetailDto getRevenueReportDetailById(int id) {
        RevenueReportDetail reportDetail = revenueReportDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report Detail with this ID cannot be found"));
        return revenueReportDetailMapper.toResponseDto(reportDetail);
    }

    public ResponseRevenueReportDetailDto createRevenueReportDetail(RevenueReportDetailDto revenueReportDetailDto) {
        RevenueReportDetail reportDetail = revenueReportDetailMapper.toEntity(revenueReportDetailDto);
        revenueReportDetailRepository.save(reportDetail);
        return revenueReportDetailMapper.toResponseDto(reportDetail);
    }

    public ResponseRevenueReportDetailDto updateRevenueReportDetail(int id, RevenueReportDetailDto revenueReportDetailDto) {
        RevenueReportDetail reportDetail = revenueReportDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report Detail with this ID cannot be found"));
        revenueReportDetailMapper.updateEntityFromDto(revenueReportDetailDto, reportDetail);
        revenueReportDetailRepository.save(reportDetail);
        return revenueReportDetailMapper.toResponseDto(reportDetail);
    }

    public void deleteRevenueReportDetailById(int id) {
        RevenueReportDetail reportDetail = revenueReportDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report Detail with this ID cannot be found"));
        revenueReportDetailRepository.delete(reportDetail);
    }
}
