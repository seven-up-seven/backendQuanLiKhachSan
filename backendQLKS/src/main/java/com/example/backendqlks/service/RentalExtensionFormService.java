package com.example.backendqlks.service;

import com.example.backendqlks.dao.RentalExtensionFormRepository;
import com.example.backendqlks.dto.rentalextensionform.RentalExtensionFormDto;
import com.example.backendqlks.dto.rentalextensionform.ResponseRentalExtensionFormDto;
import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.mapper.RentalExtensionFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RentalExtensionFormService {
    private final RentalExtensionFormRepository rentalExtensionFormRepository;
    private final RentalExtensionFormMapper rentalExtensionFormMapper;

    public RentalExtensionFormService(RentalExtensionFormMapper rentalExtensionFormMapper,
                                      RentalExtensionFormRepository rentalExtensionFormRepository) {
        this.rentalExtensionFormMapper = rentalExtensionFormMapper;
        this.rentalExtensionFormRepository = rentalExtensionFormRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseRentalExtensionFormDto> getAllRentalExtensionForms() {
        List<RentalExtensionForm> extensionForms = rentalExtensionFormRepository.findAll();
        return rentalExtensionFormMapper.toResponseDtoList(extensionForms);
    }

    @Transactional(readOnly = true)
    public ResponseRentalExtensionFormDto getRentalExtensionFormById(int id) {
        RentalExtensionForm extensionForm = rentalExtensionFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Extension Form with this ID cannot be found"));
        return rentalExtensionFormMapper.toResponseDto(extensionForm);
    }

    public ResponseRentalExtensionFormDto createRentalExtensionForm(RentalExtensionFormDto extensionFormDto) {
        RentalExtensionForm extensionForm = rentalExtensionFormMapper.toEntity(extensionFormDto);
        rentalExtensionFormRepository.save(extensionForm);
        return rentalExtensionFormMapper.toResponseDto(extensionForm);
        //TODO: Check for rental form or rental form detail???
    }

    public ResponseRentalExtensionFormDto updateRentalExtensionForm(int id, RentalExtensionFormDto extensionFormDto) {
        RentalExtensionForm extensionForm = rentalExtensionFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Extension Form with this ID cannot be found"));
        rentalExtensionFormMapper.updateEntityFromDto(extensionFormDto, extensionForm);
        rentalExtensionFormRepository.save(extensionForm);
        return rentalExtensionFormMapper.toResponseDto(extensionForm);
    }

    public void deleteRentalExtensionFormById(int id) {
        RentalExtensionForm extensionForm = rentalExtensionFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Extension Form with this ID cannot be found"));
        //Delete this rental extension form in related rental form
        RentalForm rentalForm=extensionForm.getRentalForm().getRentalForm();
        rentalForm.getRentalExtensionForms().remove(extensionForm);
        rentalExtensionFormRepository.delete(extensionForm);
    }
}
