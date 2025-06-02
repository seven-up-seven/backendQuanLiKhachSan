package com.example.backendqlks.service;

import com.example.backendqlks.dao.InvoiceDetailRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.dto.invoiceDetail.ResponseInvoiceDetailDto;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.InvoiceDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class InvoiceDetailService {
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final RentalFormRepository rentalFormRepository;
    private final InvoiceDetailMapper invoiceDetailMapper;

    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository,
                                InvoiceDetailMapper invoiceDetailMapper,
                                RentalFormRepository rentalFormRepository) {
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.invoiceDetailMapper = invoiceDetailMapper;
        this.rentalFormRepository = rentalFormRepository;
    }

    @Transactional(readOnly = true)
    public ResponseInvoiceDetailDto get(int id) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
        return invoiceDetailMapper.toResponseDto(existingInvoiceDetail);
    }

    @Transactional(readOnly = true)
    public List<ResponseInvoiceDetailDto> getAll() {
        var allInvoiceDetails = invoiceDetailRepository.findAll();
        return invoiceDetailMapper.toResponseDtoList(allInvoiceDetails);
    }

    public ResponseInvoiceDetailDto create(InvoiceDetailDto invoiceDetailDto) {
        var rentalForm = rentalFormRepository.findById(invoiceDetailDto.getRentalFormId())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect rental form id"));
//TODO: check xem rental form tương ứng hợp lệ không, nếu không (đã trả rồi) thì quăng ra lỗi
//        if(!rentalForm.getIsPaidAt()){
//            throw new IllegalArgumentException("")
//        }
        var invoiceDetail = invoiceDetailMapper.toEntity(invoiceDetailDto);
        invoiceDetailRepository.save(invoiceDetail);
        return invoiceDetailMapper.toResponseDto(invoiceDetail);
    }

    @Transactional
    public ResponseInvoiceDetailDto update(int id,InvoiceDetailDto invoiceDetailDto) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
        invoiceDetailMapper.updateEntityFromDto(invoiceDetailDto, existingInvoiceDetail);
        invoiceDetailRepository.save(existingInvoiceDetail);
        return invoiceDetailMapper.toResponseDto(existingInvoiceDetail);
    }

    //TODO: modify later
    @Transactional
    public void delete(int id) {
        var existingInvoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect invoice detail id"));
            invoiceDetailRepository.delete(existingInvoiceDetail);
    }
}
