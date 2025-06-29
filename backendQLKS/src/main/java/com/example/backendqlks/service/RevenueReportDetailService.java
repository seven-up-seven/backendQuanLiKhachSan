package com.example.backendqlks.service;

import com.example.backendqlks.dao.RevenueReportDetailRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.revenuereportdetail.ResponseRevenueReportDetailDto;
import com.example.backendqlks.dto.revenuereportdetail.RevenueReportDetailDto;
import com.example.backendqlks.entity.RevenueReport;
import com.example.backendqlks.entity.RevenueReportDetail;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.RevenueReportDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RevenueReportDetailService {
    private final RevenueReportDetailRepository revenueReportDetailRepository;
    private final RevenueReportDetailMapper revenueReportDetailMapper;
    private final HistoryService historyService;

    public RevenueReportDetailService(RevenueReportDetailMapper revenueReportDetailMapper,
                                      RevenueReportDetailRepository revenueReportDetailRepository,
                                      HistoryService historyService) {
        this.revenueReportDetailMapper = revenueReportDetailMapper;
        this.revenueReportDetailRepository = revenueReportDetailRepository;
        this.historyService = historyService;
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

    public ResponseRevenueReportDetailDto createRevenueReportDetail(RevenueReportDetailDto revenueReportDetailDto, int impactorId, String impactor) {
        RevenueReportDetail reportDetail = revenueReportDetailMapper.toEntity(revenueReportDetailDto);
        revenueReportDetailRepository.save(reportDetail);
        String content = String.format("Tổng doanh thu phòng: %.2f; Mã báo cáo doanh thu: %d; Mã loại phòng: %d",
                reportDetail.getTotalRoomRevenue(),
                reportDetail.getRevenueReportId(),
                reportDetail.getRoomTypeId());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Chi tiết báo cáo doanh thu")
                .affectedObjectId(reportDetail.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return revenueReportDetailMapper.toResponseDto(reportDetail);
    }

    public ResponseRevenueReportDetailDto updateRevenueReportDetail(int id, RevenueReportDetailDto revenueReportDetailDto, int impactorId, String impactor) {
        RevenueReportDetail reportDetail = revenueReportDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report Detail with this ID cannot be found"));
        double oldRevenue = reportDetail.getTotalRoomRevenue();
        int oldReportId = reportDetail.getRevenueReportId();
        int oldRoomTypeId = reportDetail.getRoomTypeId();
        revenueReportDetailMapper.updateEntityFromDto(revenueReportDetailDto, reportDetail);
        revenueReportDetailRepository.save(reportDetail);
        StringBuilder contentBuilder = new StringBuilder();
        if (oldRevenue != revenueReportDetailDto.getTotalRoomRevenue()) {
            contentBuilder.append(String.format("Tổng doanh thu phòng: %.2f -> %.2f; ", oldRevenue, revenueReportDetailDto.getTotalRoomRevenue()));
        }
        if (oldReportId != revenueReportDetailDto.getRevenueReportId()) {
            contentBuilder.append(String.format("Mã báo cáo doanh thu: %d -> %d; ", oldReportId, revenueReportDetailDto.getRevenueReportId()));
        }
        if (oldRoomTypeId != revenueReportDetailDto.getRoomTypeId()) {
            contentBuilder.append(String.format("Mã loại phòng: %d -> %d; ", oldRoomTypeId, revenueReportDetailDto.getRoomTypeId()));
        }
        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Chi tiết báo cáo doanh thu")
                    .affectedObjectId(reportDetail.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }
        return revenueReportDetailMapper.toResponseDto(reportDetail);
    }

    public void deleteRevenueReportDetailById(int id, int impactorId, String impactor) {
        RevenueReportDetail reportDetail = revenueReportDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report Detail with this ID cannot be found"));
        String content = String.format("Tổng doanh thu phòng: %.2f; Mã báo cáo doanh thu: %d; Mã loại phòng: %d",
                reportDetail.getTotalRoomRevenue(),
                reportDetail.getRevenueReportId(),
                reportDetail.getRoomTypeId());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Chi tiết báo cáo doanh thu")
                .affectedObjectId(reportDetail.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        revenueReportDetailRepository.delete(reportDetail);
    }


}
