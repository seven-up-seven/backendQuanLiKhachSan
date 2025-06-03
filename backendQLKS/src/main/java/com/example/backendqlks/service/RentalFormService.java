package com.example.backendqlks.service;

import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.InvoiceDetail;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.mapper.RentalFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RentalFormService {
    private final RentalFormRepository rentalFormRepository;
    private final RentalFormMapper rentalFormMapper;

    public RentalFormService(RentalFormMapper rentalFormMapper, RentalFormRepository rentalFormRepository) {
        this.rentalFormMapper = rentalFormMapper;
        this.rentalFormRepository = rentalFormRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseRentalFormDto> getAllRentalForms() {
        List<RentalForm> rentalForms = rentalFormRepository.findAll();
        return rentalFormMapper.toResponseDtoList(rentalForms);
    }

    @Transactional(readOnly = true)
    public ResponseRentalFormDto getRentalFormById(int id) {
        RentalForm rentalForm=rentalFormRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public ResponseRentalFormDto createRentalForm(RentalFormDto rentalFormDto) {
        RentalForm rentalForm=rentalFormMapper.toEntity(rentalFormDto);
        rentalFormRepository.save(rentalForm);
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public ResponseRentalFormDto updateRentalForm(int id, RentalFormDto rentalFormDto) {
        RentalForm rentalForm=rentalFormRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        rentalFormMapper.updateEntityFromDto(rentalFormDto, rentalForm);
        rentalFormRepository.save(rentalForm);
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public void deleteRentalFormById(int id) {
        RentalForm rentalForm=rentalFormRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        //delete related foreign keys in other tables
        Staff staff=rentalForm.getStaff();
        staff.getRentalForms().remove(rentalForm);
        rentalFormRepository.delete(rentalForm);
    }
}
