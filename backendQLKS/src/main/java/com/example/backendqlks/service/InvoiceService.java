package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceDetailRepository;
import com.example.backendqlks.dao.InvoiceRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.invoice.InvoiceDto;
import com.example.backendqlks.dto.invoice.ResponseInvoiceDto;
import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.InvoiceDetailMapper;
import com.example.backendqlks.mapper.InvoiceMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@Transactional
@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final RentalFormRepository rentalFormRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final HistoryService historyService;
    private final RoomRepository roomRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceMapper invoiceMapper,
                          RentalFormRepository rentalFormRepository,
                          InvoiceDetailRepository invoiceDetailRepository,
                          HistoryService historyService, RoomRepository roomRepository) {
        this.invoiceMapper = invoiceMapper;
        this.invoiceRepository = invoiceRepository;
        this.rentalFormRepository = rentalFormRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.historyService = historyService;
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public ResponseInvoiceDto get(int invoiceId){
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        return invoiceMapper.toResponseDto(existingInvoice);
    }

    @Transactional(readOnly = true)
    public Page<ResponseInvoiceDto> getAllPage(Pageable pageable){
        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);
        List<ResponseInvoiceDto> invoices=invoiceMapper.toResponseDtoList(invoicePage.getContent());
        return new PageImpl<>(invoices, pageable, invoicePage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseInvoiceDto> getAll(){
        return invoiceMapper.toResponseDtoList(invoiceRepository.findAll());
    }

    //TODO: add try catch
    public ResponseInvoiceDto create(InvoiceDto invoiceDto, int impactorId, String impactor) {
        var newInvoice = invoiceMapper.toEntity(invoiceDto);
        newInvoice.setStaffId(impactorId);
        invoiceRepository.save(newInvoice);
        String content = String.format(
                "Mã khách thanh toán: %d; Mã nhân viên tạo hóa đơn: %d; Tổng chi phí đặt phòng: %.2f",
                invoiceDto.getPayingGuestId(),
                invoiceDto.getStaffId(),
                invoiceDto.getTotalReservationCost()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Hóa đơn")
                .affectedObjectId(newInvoice.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return invoiceMapper.toResponseDto(newInvoice);
    }

    //TODO: add try catch
    public ResponseInvoiceDto update(int invoiceId, InvoiceDto invoiceDto, int impactorId, String impactor) {
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        invoiceMapper.updateEntityFromDto(invoiceDto, existingInvoice);
        invoiceRepository.save(existingInvoice);
        List<String> changes = new ArrayList<>();
        if (invoiceDto.getPayingGuestId() != existingInvoice.getPayingGuestId()) {
            changes.add(String.format("Mã khách thanh toán: %d -> %d",
                    existingInvoice.getPayingGuestId(), invoiceDto.getPayingGuestId()));
        }
        if (invoiceDto.getStaffId() != existingInvoice.getStaffId()) {
            changes.add(String.format("Mã nhân viên: %d -> %d",
                    existingInvoice.getStaffId(), invoiceDto.getStaffId()));
        }
        if (Double.compare(invoiceDto.getTotalReservationCost(), existingInvoice.getTotalReservationCost()) != 0) {
            changes.add(String.format("Tổng chi phí: %.2f -> %.2f",
                    existingInvoice.getTotalReservationCost(), invoiceDto.getTotalReservationCost()));
        }
        if (!changes.isEmpty()) {
            String content = String.join("; ", changes);
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Hóa đơn")
                    .affectedObjectId(existingInvoice.getId())
                    .action(Action.UPDATE)
                    .content(content)
                    .build();
            historyService.create(history);
        }
        return invoiceMapper.toResponseDto(existingInvoice);
    }

    //TODO: modify later
    public void delete(int invoiceId, int impactorId, String impactor) {
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        String content = String.format(
                "Mã khách thanh toán: %d; Mã nhân viên tạo hóa đơn: %d; Tổng chi phí đặt phòng: %.2f",
                existingInvoice.getPayingGuestId(),
                existingInvoice.getStaffId(),
                existingInvoice.getTotalReservationCost()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Hóa đơn")
                .affectedObjectId(existingInvoice.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        invoiceRepository.delete(existingInvoice);
    }

    public ResponseInvoiceDto reCalculateInvoice(int invoiceId, int impactorId, String impactor) {
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        var details = existingInvoice.getInvoiceDetails();
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Invoice has no details to recalculate");
        }
        double totalAmount = 0;
        StringBuilder detailUpdateHistory = new StringBuilder();
        double previousTotalAmount = existingInvoice.getTotalReservationCost();
        for (var detail : details) {
            double oldCost = detail.getReservationCost();
            var rentalForm = rentalFormRepository.findById(detail.getRentalForm().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Rental Form with this ID cannot be found"));
            var rentalDays = rentalForm.getNumberOfRentalDays();
            var pricePerDay = rentalForm.getRoom().getRoomType().getPrice();
            var amount = rentalDays * pricePerDay;
            detail.setReservationCost(amount);
            invoiceDetailRepository.save(detail);
            if (Double.compare(oldCost, amount) != 0) {
                detailUpdateHistory.append(String.format("- Chi tiết #%d: %.2f → %.2f\n", detail.getId(), oldCost, amount));
            }
            totalAmount += amount;
        }
        if (!detailUpdateHistory.isEmpty()) {
            HistoryDto detailHistory = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Chi tiết hóa đơn")
                    .affectedObjectId(existingInvoice.getId())
                    .action(Action.UPDATE)
                    .content("Cập nhật chi phí các chi tiết hóa đơn:\n" + detailUpdateHistory.toString().trim())
                    .build();
            historyService.create(detailHistory);
        }
        existingInvoice.setTotalReservationCost(totalAmount);
        invoiceRepository.save(existingInvoice);
        if (Double.compare(previousTotalAmount, totalAmount) != 0) {
            String invoiceContent = String.format("Tổng chi phí: %.2f → %.2f", previousTotalAmount, totalAmount);
            HistoryDto invoiceHistory = HistoryDto.builder()
                    .impactor("Hệ thống")
                    .impactorId(existingInvoice.getStaffId())
                    .affectedObject("Hóa đơn")
                    .affectedObjectId(existingInvoice.getId())
                    .action(Action.UPDATE)
                    .content(invoiceContent)
                    .build();
            historyService.create(invoiceHistory);
        }
        return invoiceMapper.toResponseDto(existingInvoice);
    }


}