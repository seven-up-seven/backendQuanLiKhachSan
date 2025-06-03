package com.example.backendqlks.service;

import com.example.backendqlks.dao.RentalFormDetailRepository;
import com.example.backendqlks.dto.rentalformdetail.RentalFormDetailDto;
import com.example.backendqlks.dto.rentalformdetail.ResponseRentalFormDetailDto;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.RentalFormDetail;
import com.example.backendqlks.mapper.RentalFormDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RentalFormDetailService {
    private final RentalFormDetailRepository rentalFormDetailRepository;
    private final RentalFormDetailMapper rentalFormDetailMapper;

    public RentalFormDetailService(RentalFormDetailRepository rentalFormDetailRepository, RentalFormDetailMapper rentalFormDetailMapper) {
        this.rentalFormDetailRepository = rentalFormDetailRepository;
        this.rentalFormDetailMapper = rentalFormDetailMapper;
    }

    @Transactional(readOnly = true)
    public List<ResponseRentalFormDetailDto> getAllRentalFormDetails() {
        List<RentalFormDetail> rentalFormDetails = rentalFormDetailRepository.findAll();
        return rentalFormDetailMapper.toResponseDtoList(rentalFormDetails);
    }

    @Transactional(readOnly = true)
    public ResponseRentalFormDetailDto getRentalFormDetailById(int id) {
        RentalFormDetail rentalFormDetail = rentalFormDetailRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        return rentalFormDetailMapper.toResponseDto(rentalFormDetail);
    }

    public ResponseRentalFormDetailDto createRentalFormDetail(RentalFormDetailDto rentalFormDetailDto) {
        RentalFormDetail rentalFormDetail = rentalFormDetailMapper.toEntity(rentalFormDetailDto);
        rentalFormDetailRepository.save(rentalFormDetail);
        return rentalFormDetailMapper.toResponseDto(rentalFormDetail);
    }

    public ResponseRentalFormDetailDto updateRentalFormDetail(int id, RentalFormDetailDto rentalFormDetailDto) {
        RentalFormDetail rentalFormDetail = rentalFormDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form Detail with this ID cannot be found"));
        rentalFormDetailMapper.updateEntityFromDto(rentalFormDetailDto, rentalFormDetail);
        rentalFormDetailRepository.save(rentalFormDetail);
        return rentalFormDetailMapper.toResponseDto(rentalFormDetail);
    }

    public void deleteRentalFormDetailById(int id) {
        RentalFormDetail rentalFormDetail = rentalFormDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form Detail with this ID cannot be found"));
        //delete this rental form detail in the related rental form
        RentalForm rentalForm=rentalFormDetail.getRentalForm();
        rentalForm.getRentalFormDetails().remove(rentalFormDetail);
        rentalFormDetailRepository.delete(rentalFormDetail);
    }
}
