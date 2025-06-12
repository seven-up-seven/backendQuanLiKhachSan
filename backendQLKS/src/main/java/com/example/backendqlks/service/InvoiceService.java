package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceDetailRepository;
import com.example.backendqlks.dao.InvoiceRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.invoice.InvoiceDto;
import com.example.backendqlks.dto.invoice.ResponseInvoiceDto;
import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.entity.Invoice;
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

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceMapper invoiceMapper,
                          RentalFormRepository rentalFormRepository,
                          InvoiceDetailRepository invoiceDetailRepository){
        this.invoiceMapper = invoiceMapper;
        this.invoiceRepository = invoiceRepository;
        this.rentalFormRepository = rentalFormRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Transactional(readOnly = true)
    public ResponseInvoiceDto get(int invoiceId){
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        return invoiceMapper.toResponseDto(existingInvoice);
    }

    @Transactional(readOnly = true)
    public Page<ResponseInvoiceDto> getAll(Pageable pageable){
        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);
        List<ResponseInvoiceDto> invoices=invoiceMapper.toResponseDtoList(invoicePage.getContent());
        return new PageImpl<>(invoices, pageable, invoicePage.getTotalElements());
    }

    //TODO: add try catch
    public ResponseInvoiceDto create(InvoiceDto invoiceDto){
        var newInvoice = invoiceMapper.toEntity(invoiceDto);
        invoiceRepository.save(newInvoice);
        return invoiceMapper.toResponseDto(newInvoice);
    }

    //TODO: add try catch
    public ResponseInvoiceDto update(int invoiceId, InvoiceDto invoiceDto){
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        invoiceMapper.updateEntityFromDto(invoiceDto, existingInvoice);
        invoiceRepository.save(existingInvoice);
        return invoiceMapper.toResponseDto(existingInvoice);
    }

    //TODO: modify later
    public void delete(int invoiceId){
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        invoiceRepository.delete(existingInvoice);
    }
    public ResponseInvoiceDto reCalculateInvoice(int invoiceId) {
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        var details = existingInvoice.getInvoiceDetails();
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Invoice has no details to recalculate");
        }
        double totalAmount = 0;
        for (var detail : details) {
            var rentalForm = rentalFormRepository.findById(detail.getRentalForm().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Rental Form with this ID cannot be found"));
            var rentalDays = rentalForm.getNumberOfRentalDays();
            var pricePerDay = rentalForm.getRoom().getRoomType().getPrice();
            var amount = rentalDays * pricePerDay;
            detail.setReservationCost(amount);
            invoiceDetailRepository.save(detail);
            totalAmount += amount;
        }
        existingInvoice.setTotalReservationCost(totalAmount);
        invoiceRepository.save(existingInvoice);
        return invoiceMapper.toResponseDto(existingInvoice);
    }
}