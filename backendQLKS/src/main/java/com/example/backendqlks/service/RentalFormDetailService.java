package com.example.backendqlks.service;

import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dao.RentalFormDetailRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.rentalformdetail.RentalFormDetailDto;
import com.example.backendqlks.dto.rentalformdetail.ResponseRentalFormDetailDto;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.RentalFormDetail;
import com.example.backendqlks.entity.enums.Action;
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
    private final HistoryService historyService;

    public RentalFormDetailService(RentalFormDetailRepository rentalFormDetailRepository,
                                   RentalFormDetailMapper rentalFormDetailMapper,
                                   RentalFormRepository rentalFormRepository,
                                   GuestRepository guestRepository,
                                   HistoryService historyService) {
        this.rentalFormDetailRepository = rentalFormDetailRepository;
        this.rentalFormDetailMapper = rentalFormDetailMapper;
        this.rentalFormRepository = rentalFormRepository;
        this.guestRepository = guestRepository;
        this.historyService = historyService;
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

    public ResponseRentalFormDetailDto createRentalFormDetail(RentalFormDetailDto rentalFormDetailDto, int impactorId, String impactor) {
        RentalFormDetail rentalFormDetail = rentalFormDetailMapper.toEntity(rentalFormDetailDto);
        rentalFormDetailRepository.save(rentalFormDetail);
        String content = String.format("ID phiếu thuê: %d; ID khách: %d", rentalFormDetail.getRentalFormId(), rentalFormDetail.getGuestId());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Chi tiết phiếu thuê")
                .affectedObjectId(rentalFormDetail.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return rentalFormDetailMapper.toResponseDto(rentalFormDetail);
    }

    public ResponseRentalFormDetailDto updateRentalFormDetail(int id, RentalFormDetailDto rentalFormDetailDto, int impactorId, String impactor) {
        RentalFormDetail rentalFormDetail = rentalFormDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form Detail with this ID cannot be found"));
        StringBuilder contentBuilder = new StringBuilder();
        if (rentalFormDetail.getRentalFormId() != rentalFormDetailDto.getRentalFormId()) {
            contentBuilder.append(String.format("ID phiếu thuê: %d -> %d; ", rentalFormDetail.getRentalFormId(), rentalFormDetailDto.getRentalFormId()));
        }
        if (rentalFormDetail.getGuestId() != rentalFormDetailDto.getGuestId()) {
            contentBuilder.append(String.format("ID khách: %d -> %d; ", rentalFormDetail.getGuestId(), rentalFormDetailDto.getGuestId()));
        }
        if (contentBuilder.length() > 0) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Chi tiết phiếu thuê")
                    .affectedObjectId(rentalFormDetail.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }
        rentalFormDetailMapper.updateEntityFromDto(rentalFormDetailDto, rentalFormDetail);
        rentalFormDetailRepository.save(rentalFormDetail);
        return rentalFormDetailMapper.toResponseDto(rentalFormDetail);
    }

    public void deleteRentalFormDetailById(int id, int impactorId, String impactor) {
        RentalFormDetail rentalFormDetail = rentalFormDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form Detail with this ID cannot be found"));
        //delete this rental form detail in the related rental form
        RentalForm rentalForm = rentalFormDetail.getRentalForm();
        rentalForm.getRentalFormDetails().remove(rentalFormDetail);
        String content = String.format("ID phiếu thuê: %d; ID khách: %d", rentalFormDetail.getRentalFormId(), rentalFormDetail.getGuestId());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Chi tiết phiếu thuê")
                .affectedObjectId(rentalFormDetail.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        rentalFormDetailRepository.delete(rentalFormDetail);
    }

    public void createRentalFormDetailList(List<RentalFormDetailDto> rentalFormDetailDtos, int rentalFormId, int impactorId, String impactor) {
        for (RentalFormDetailDto rentalFormDetailDto : rentalFormDetailDtos) {
            RentalFormDetail rentalFormDetail = rentalFormDetailMapper.toEntity(rentalFormDetailDto);
            rentalFormDetail.setRentalFormId(rentalFormId);
            rentalFormDetailRepository.save(rentalFormDetail);

            String content = String.format("ID phiếu thuê: %d; ID khách: %d", rentalFormDetail.getRentalFormId(), rentalFormDetail.getGuestId());
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Chi tiết phiếu thuê")
                    .affectedObjectId(rentalFormDetail.getId())
                    .action(Action.CREATE)
                    .content(content)
                    .build();
            historyService.create(history);
        }
    }

    public List<ResponseRentalFormDetailDto> createRentalFormDetails(int rentalFormId, List<Integer> guestIds, int impactorId, String impactor) {
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
            String content = String.format("ID phiếu thuê: %d; ID khách: %d", rentalFormDetail.getRentalFormId(), rentalFormDetail.getGuestId());
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Chi tiết phiếu thuê")
                    .affectedObjectId(rentalFormDetail.getId())
                    .action(Action.CREATE)
                    .content(content)
                    .build();
            historyService.create(history);
            listResult.add(rentalFormDetailMapper.toResponseDto(rentalFormDetail));
        }
        return listResult;
    }

    @Transactional(readOnly = true)
    public List<ResponseRentalFormDetailDto> getRentalFormDetailsByUserId(int userId) {
        return rentalFormDetailMapper.toResponseDtoList(rentalFormDetailRepository.findRentalFormDetailsByGuestId(userId));
    }
}
