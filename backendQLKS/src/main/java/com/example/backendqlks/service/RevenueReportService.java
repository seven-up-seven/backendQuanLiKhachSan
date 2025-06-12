package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceRepository;
import com.example.backendqlks.dao.RevenueReportDetailRepository;
import com.example.backendqlks.dao.RevenueReportRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.revenuereport.ResponseRevenueReportDto;
import com.example.backendqlks.dto.revenuereport.RevenueReportDto;
import com.example.backendqlks.entity.RevenueReport;
import com.example.backendqlks.entity.RevenueReportDetail;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.RevenueReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Transactional
@Service
public class RevenueReportService {
    private final RevenueReportRepository revenueReportRepository;
    private final RevenueReportMapper revenueReportMapper;
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    private final RevenueReportDetailRepository revenueReportDetailRepository;
    private final HistoryService historyService;

    public RevenueReportService(RevenueReportMapper revenueReportMapper,
                                RevenueReportRepository revenueReportRepository,
                                InvoiceService invoiceService,
                                InvoiceRepository invoiceRepository,
                                RevenueReportDetailRepository revenueReportDetailRepository,
                                HistoryService historyService) {
        this.revenueReportMapper = revenueReportMapper;
        this.revenueReportRepository = revenueReportRepository;
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.historyService = historyService;
        this.revenueReportDetailRepository = revenueReportDetailRepository;
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

    public ResponseRevenueReportDto createRevenueReport(RevenueReportDto revenueReportDto, int impactorId, String impactor) {
        RevenueReport revenueReport = revenueReportMapper.toEntity(revenueReportDto);
        var invoices = invoiceRepository.findAll();
        Hashtable<Integer, Double> totalRoomRevenueByRoomTypeId = new Hashtable<>();
        for (var invoice : invoices) {
            if (invoice.getCreatedAt().getYear() == revenueReport.getYear()) {
                if( invoice.getCreatedAt().getMonthValue() == revenueReport.getMonth()) {
                    for (var invoiceDetail : invoice.getInvoiceDetails()) {
                        int roomTypeId = invoiceDetail.getRentalForm().getRoom().getRoomTypeId();
                        double totalRoomRevenue = invoiceDetail.getReservationCost();
                        totalRoomRevenueByRoomTypeId.put(roomTypeId,
                                totalRoomRevenueByRoomTypeId.getOrDefault(roomTypeId, 0.0) + totalRoomRevenue);
                    }
                }
            }
        }
        var listDetail = new ArrayList<RevenueReportDetail>();
        for (var entry : totalRoomRevenueByRoomTypeId.entrySet()) {
            int roomTypeId = entry.getKey();
            double totalRoomRevenue = entry.getValue();
            var revenueReportDetail = new RevenueReportDetail();
            revenueReportDetail.setRoomTypeId(roomTypeId);
            revenueReportDetail.setTotalRoomRevenue(totalRoomRevenue);
            listDetail.add(revenueReportDetail);
        }
        revenueReport.setTotalMonthRevenue(totalRoomRevenueByRoomTypeId.values().stream().mapToDouble(Double::doubleValue).sum());
        revenueReportRepository.save(revenueReport);
        for (RevenueReportDetail revenueReportDetail : listDetail) {
            revenueReportDetail.setRevenueReportId(revenueReport.getId());
            revenueReportDetailRepository.save(revenueReportDetail);
            String content = String.format("Năm: %d; Tháng: %d; Tổng doanh thu tháng: %.2f",
                    revenueReport.getYear(),
                    revenueReport.getMonth(),
                    revenueReport.getTotalMonthRevenue());
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Báo cáo doanh thu")
                    .affectedObjectId(revenueReport.getId())
                    .action(Action.CREATE)
                    .content(content)
                    .build();
            historyService.create(history);
        }
        return revenueReportMapper.toResponseDto(revenueReport);
    }

    public ResponseRevenueReportDto updateRevenueReport(int id, RevenueReportDto revenueReportDto, int impactorId, String impactor) {
        RevenueReport revenueReport = revenueReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report with this ID cannot be found"));
        short oldYear = revenueReport.getYear();
        byte oldMonth = revenueReport.getMonth();
        double oldTotal = revenueReport.getTotalMonthRevenue();
        revenueReportMapper.updateEntityFromDto(revenueReportDto, revenueReport);
        revenueReportRepository.save(revenueReport);
        StringBuilder contentBuilder = new StringBuilder();
        if (oldYear != revenueReportDto.getYear()) {
            contentBuilder.append(String.format("Năm: %d -> %d; ", oldYear, revenueReportDto.getYear()));
        }
        if (oldMonth != revenueReportDto.getMonth()) {
            contentBuilder.append(String.format("Tháng: %d -> %d; ", oldMonth, revenueReportDto.getMonth()));
        }
        if (oldTotal != revenueReportDto.getTotalMonthRevenue()) {
            contentBuilder.append(String.format("Tổng doanh thu tháng: %.2f -> %.2f; ", oldTotal, revenueReportDto.getTotalMonthRevenue()));
        }
        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Báo cáo doanh thu")
                    .affectedObjectId(revenueReport.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }
        return revenueReportMapper.toResponseDto(revenueReport);
    }

    public void deleteRevenueReportById(int id, int impactorId, String impactor) {
        RevenueReport revenueReport = revenueReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Revenue Report with this ID cannot be found"));
        String content = String.format("Năm: %d; Tháng: %d; Tổng doanh thu tháng: %.2f",
                revenueReport.getYear(),
                revenueReport.getMonth(),
                revenueReport.getTotalMonthRevenue());

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Báo cáo doanh thu")
                .affectedObjectId(revenueReport.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        revenueReportRepository.delete(revenueReport);
    }
}
