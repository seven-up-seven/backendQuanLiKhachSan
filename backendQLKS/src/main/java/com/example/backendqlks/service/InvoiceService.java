package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceDetailRepository;
import com.example.backendqlks.dao.InvoiceRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dto.invoice.InvoiceDto;
import com.example.backendqlks.dto.invoice.ResponseInvoiceDto;
import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.mapper.InvoiceDetailMapper;
import com.example.backendqlks.mapper.InvoiceMapper;
import jakarta.validation.Valid;
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
    private final InvoiceDetailMapper invoiceDetailMapper;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceMapper invoiceMapper,
                          RentalFormRepository rentalFormRepository, InvoiceDetailMapper invoiceDetailMapper, InvoiceDetailRepository invoiceDetailRepository){
        this.invoiceMapper = invoiceMapper;
        this.invoiceRepository = invoiceRepository;
        this.rentalFormRepository = rentalFormRepository;
        this.invoiceDetailMapper = invoiceDetailMapper;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Transactional(readOnly = true)
    public ResponseInvoiceDto get(int invoiceId){
        var existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice id"));
        return invoiceMapper.toResponseDto(existingInvoice);
    }

    @Transactional(readOnly = true)
    public List<ResponseInvoiceDto> getAll(){
        var allInvoice = invoiceRepository.findAll();
        return invoiceMapper.toResponseDtoList(allInvoice);
    }

    //TODO: add try catch
    //after checking all detail, we will create an invoice
    public ResponseInvoiceDto create(InvoiceDto invoiceDto, List<InvoiceDetailDto> invoiceDetailDtos){
        invoiceDetailDtos.stream().filter(Objects::nonNull)
                .forEach(invoiceDetailDto -> {
                    var rentalForm = rentalFormRepository.findById(invoiceDetailDto.getRentalFormId())
                            .orElseThrow(() -> new IllegalArgumentException("Incorrect rental form id"));
//TODO: check xem rental form tương ứng hợp lệ không, nếu không (đã trả rồi) thì quăng ra lỗi
//        if(rentalForm.getIsPaidAt()){
//            throw new IllegalArgumentException("Rental form has been paid")
//        }
                    var invoiceDetail = invoiceDetailMapper.toEntity(invoiceDetailDto);
                    invoiceDetailRepository.save(invoiceDetail);
                });

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
    }
}