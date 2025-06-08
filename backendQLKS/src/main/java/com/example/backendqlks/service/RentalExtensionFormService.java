package com.example.backendqlks.service;

import com.example.backendqlks.dao.RentalExtensionFormRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dto.rentalextensionform.RentalExtensionFormDto;
import com.example.backendqlks.dto.rentalextensionform.ResponseRentalExtensionFormDto;
import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.mapper.RentalExtensionFormMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RentalExtensionFormService {
    private final RentalExtensionFormRepository rentalExtensionFormRepository;
    private final RentalExtensionFormMapper rentalExtensionFormMapper;
    private final RentalFormRepository rentalFormRepository;

    public RentalExtensionFormService(RentalExtensionFormMapper rentalExtensionFormMapper,
                                      RentalExtensionFormRepository rentalExtensionFormRepository, RentalFormRepository rentalFormRepository) {
        this.rentalExtensionFormMapper = rentalExtensionFormMapper;
        this.rentalExtensionFormRepository = rentalExtensionFormRepository;
        this.rentalFormRepository = rentalFormRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseRentalExtensionFormDto> getAllRentalExtensionForms(Pageable pageable) {
        Page<RentalExtensionForm> extensionForms = rentalExtensionFormRepository.findAll(pageable);
        var responseDtos = new ArrayList<ResponseRentalExtensionFormDto>();
        for (var extensionForm : extensionForms) {
            RentalForm rentalForm = extensionForm.getRentalForm();
            int totalDaysExtended = rentalForm.getRentalExtensionForms().stream()
                    .mapToInt(RentalExtensionForm::getNumberOfRentalDays)
                    .sum();
            var responseDto = rentalExtensionFormMapper.toResponseDto(extensionForm);
            responseDto.setDayRemains(totalDaysExtended);
            responseDtos.add(responseDto);
        }
        return new PageImpl<>(responseDtos, pageable, extensionForms.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ResponseRentalExtensionFormDto getRentalExtensionFormById(int id) {
        RentalExtensionForm extensionForm = rentalExtensionFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Extension Form with this ID cannot be found"));
        var rentalForm = extensionForm.getRentalForm();
        var rentalExtensionList = rentalForm.getRentalExtensionForms();
        int totalDaysExtended = rentalExtensionList.stream()
                .mapToInt(RentalExtensionForm::getNumberOfRentalDays)
                .sum();
        ResponseRentalExtensionFormDto responseDto = rentalExtensionFormMapper.toResponseDto(extensionForm);
        responseDto.setDayRemains(totalDaysExtended);
        return responseDto;
    }

    //TODO: check tổng số ngày trong rental extension form của rental form có vượt quá số ngày tối đa không?
    // nếu có thì không cho tạo, nếu không thì cho tạo số ngày <= số ngày tối đa - tổng ngày đã gia hạn
    public ResponseRentalExtensionFormDto createRentalExtensionForm(RentalExtensionFormDto extensionFormDto) {
        RentalExtensionForm extensionForm = rentalExtensionFormMapper.toEntity(extensionFormDto);
        rentalExtensionFormRepository.save(extensionForm);
        return rentalExtensionFormMapper.toResponseDto(extensionForm);

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
        rentalExtensionFormRepository.delete(extensionForm);
    }

    public int getDayRemains(int rentalFormId) {
        RentalForm rentalForm = rentalFormRepository
                .findById(rentalFormId)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form with this ID cannot be found"));
        int totalDaysExtended = rentalForm.getRentalExtensionForms().stream()
                .mapToInt(RentalExtensionForm::getNumberOfRentalDays)
                .sum();
        var finalPaidTime = rentalForm.getRentalDate().plusDays(5);
        if(LocalDateTime.now().isAfter(finalPaidTime))
            return 0; // Rental form is already paid, no days remain for extension
        return 5 - totalDaysExtended;
    }

    public List<ResponseRentalExtensionFormDto> getRentalExtensionFormsByRentalFormId(int rentalFormId) {
        RentalForm rentalForm = rentalFormRepository
                .findById(rentalFormId)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form with this ID cannot be found"));
        List<RentalExtensionForm> extensionForms = rentalExtensionFormRepository.findRentalExtensionFormsByRentalFormId(rentalFormId);
        return rentalExtensionFormMapper.toResponseDtoList(extensionForms);
    }
}
