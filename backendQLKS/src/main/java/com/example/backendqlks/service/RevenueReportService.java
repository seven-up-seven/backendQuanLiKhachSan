package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceRepository;
import com.example.backendqlks.dao.RevenueReportDetailRepository;
import com.example.backendqlks.dao.RevenueReportRepository;
import com.example.backendqlks.dto.revenuereport.ResponseRevenueReportDto;
import com.example.backendqlks.dto.revenuereport.RevenueReportDto;
import com.example.backendqlks.entity.RevenueReport;
import com.example.backendqlks.entity.RevenueReportDetail;
import com.example.backendqlks.entity.RoomType;
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

    public RevenueReportService(RevenueReportMapper revenueReportMapper,
                                RevenueReportRepository revenueReportRepository, InvoiceService invoiceService, InvoiceRepository invoiceRepository, RevenueReportDetailRepository revenueReportDetailRepository) {
        this.revenueReportMapper = revenueReportMapper;
        this.revenueReportRepository = revenueReportRepository;
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
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

    public ResponseRevenueReportDto createRevenueReport(RevenueReportDto revenueReportDto) {
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
        }
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
