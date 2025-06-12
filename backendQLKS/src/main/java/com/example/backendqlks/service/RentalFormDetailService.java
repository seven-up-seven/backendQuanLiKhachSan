package com.example.backendqlks.service;

import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dao.RentalFormDetailRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dto.rentalformdetail.RentalFormDetailDto;
import com.example.backendqlks.dto.rentalformdetail.ResponseRentalFormDetailDto;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.RentalFormDetail;
import com.example.backendqlks.mapper.RentalFormDetailMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class RentalFormDetailService {
    private final RentalFormDetailRepository rentalFormDetailRepository;
    private final RentalFormDetailMapper rentalFormDetailMapper;
    private final RentalFormRepository rentalFormRepository;
    private final GuestRepository guestRepository;

    public RentalFormDetailService(RentalFormDetailRepository rentalFormDetailRepository, RentalFormDetailMapper rentalFormDetailMapper, RentalFormRepository rentalFormRepository, GuestRepository guestRepository) {
        this.rentalFormDetailRepository = rentalFormDetailRepository;
        this.rentalFormDetailMapper = rentalFormDetailMapper;
        this.rentalFormRepository = rentalFormRepository;
        this.guestRepository = guestRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseRentalFormDetailDto> getAllRentalFormDetails(Pageable pageable) {
        Page<RentalFormDetail> rentalFormDetailPage = rentalFormDetailRepository.findAll(pageable);
        List<ResponseRentalFormDetailDto> rentalFormDetails=rentalFormDetailMapper.toResponseDtoList(rentalFormDetailPage.getContent());
        return new PageImpl<>(rentalFormDetails, pageable, rentalFormDetailPage.getTotalElements());
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
        RentalForm rentalForm = rentalFormDetail.getRentalForm();
        rentalForm.getRentalFormDetails().remove(rentalFormDetail);
        rentalFormDetailRepository.delete(rentalFormDetail);
    }

    public void createRentalFormDetailList(List<RentalFormDetailDto> rentalFormDetailDtos, int rentalFormId) {
        for (RentalFormDetailDto rentalFormDetailDto : rentalFormDetailDtos) {
            RentalFormDetail rentalFormDetail = rentalFormDetailMapper.toEntity(rentalFormDetailDto);
            rentalFormDetail.setRentalFormId(rentalFormId);
            rentalFormDetailRepository.save(rentalFormDetail);
        }
    }

    public List<ResponseRentalFormDetailDto> createRentalFormDetails(int rentalFormId, List<Integer> guestIds) {
        var listResult = new ArrayList<ResponseRentalFormDetailDto>();
        if(rentalFormRepository.findById(rentalFormId).isEmpty()) throw new IllegalArgumentException("Incorrect Rental Form ID");
        for (var guestId : guestIds) {
            if(guestRepository.findById(guestId).isEmpty())
                throw new IllegalArgumentException("Guest with ID " + guestId + " does not exist");
            if (rentalFormDetailRepository.existsByRentalFormIdAndGuestId(rentalFormId, guestId)) {
                throw new IllegalArgumentException("Rental Form Detail with this Guest ID already exists in the Rental Form");
            }
            RentalFormDetail rentalFormDetail = new RentalFormDetail();
            rentalFormDetail.setRentalFormId(rentalFormId);
            rentalFormDetail.setGuestId(guestId);
            rentalFormDetailRepository.save(rentalFormDetail);
            listResult.add(rentalFormDetailMapper.toResponseDto(rentalFormDetail));
        }
        return listResult;
    }
}
