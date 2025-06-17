package com.example.backendqlks.service;

import com.example.backendqlks.dao.RentalExtensionFormRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.dto.rentalform.SearchRentalFormDto;
import com.example.backendqlks.entity.*;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.RentalFormMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class RentalFormService {
    private final RentalFormRepository rentalFormRepository;
    private final RoomRepository roomRepository;
    private final RentalFormMapper rentalFormMapper;
    private final RentalExtensionFormRepository rentalExtensionFormRepository;
    private final HistoryService historyService;

    public RentalFormService(RentalFormMapper rentalFormMapper,
                             RentalFormRepository rentalFormRepository,
                             RoomRepository roomRepository,
                             RentalExtensionFormRepository rentalExtensionFormRepository,
                             HistoryService historyService) {
        this.rentalFormMapper = rentalFormMapper;
        this.rentalFormRepository = rentalFormRepository;
        this.roomRepository = roomRepository;
        this.rentalExtensionFormRepository = rentalExtensionFormRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public Page<ResponseRentalFormDto> getAllRentalFormPage(Pageable pageable) {
        Page<RentalForm> rentalFormPage = rentalFormRepository.findAll(pageable);
        List<ResponseRentalFormDto> rentalForms = rentalFormMapper.toResponseDtoList(rentalFormPage.getContent());
        return new PageImpl<>(rentalForms, pageable, rentalFormPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseRentalFormDto> getAllRentalForms() {
        return rentalFormMapper.toResponseDtoList(rentalFormRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ResponseRentalFormDto getRentalFormById(int id) {
        RentalForm rentalForm=rentalFormRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public ResponseRentalFormDto createRentalForm(RentalFormDto rentalFormDto, int impactorId, String impactor) {
        var room = roomRepository.findById(rentalFormDto.getRoomId());
        //kiểm tra roomid, nếu roomid thoả kiểm tra roomstate, sau đó mới tạo rental form
        if(room.isEmpty())
            throw new IllegalArgumentException("Incorrect room id");
        if(room.get().getRoomState() != RoomState.READY_TO_SERVE)
            throw new IllegalArgumentException("Room is not ready to serve");

        var rentalForm = rentalFormMapper.toEntity(rentalFormDto);
        rentalFormRepository.save(rentalForm);
        room.get().setRoomState(RoomState.BEING_RENTED);
        roomRepository.save(room.get());
        String content = String.format(
                "Phòng: %d; Nhân viên: %d; Ngày thuê: %s; Số ngày thuê: %d; Ghi chú: %s",
                rentalForm.getRoomId(),
                rentalForm.getStaffId(),
                rentalForm.getRentalDate(),
                rentalForm.getNumberOfRentalDays(),
                rentalForm.getNote()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phiếu thuê")
                .affectedObjectId(rentalForm.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public ResponseRentalFormDto updateRentalForm(int id, RentalFormDto rentalFormDto, int impactorId, String impactor) {
        RentalForm rentalForm = rentalFormRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        StringBuilder content = new StringBuilder();
        if (rentalForm.getRoomId() != rentalFormDto.getRoomId())
            content.append(String.format("Phòng: %d -> %d; ", rentalForm.getRoomId(), rentalFormDto.getRoomId()));
        if (rentalForm.getStaffId() != rentalFormDto.getStaffId())
            content.append(String.format("Nhân viên: %d -> %d; ", rentalForm.getStaffId(), rentalFormDto.getStaffId()));
        if (!Objects.equals(rentalForm.getRentalDate(), rentalFormDto.getRentalDate()))
            content.append(String.format("Ngày thuê: %s -> %s; ", rentalForm.getRentalDate(), rentalFormDto.getRentalDate()));
        if (rentalForm.getNumberOfRentalDays() != rentalFormDto.getNumberOfRentalDays())
            content.append(String.format("Số ngày thuê: %d -> %d; ", rentalForm.getNumberOfRentalDays(), rentalFormDto.getNumberOfRentalDays()));
        if (!Objects.equals(rentalForm.getNote(), rentalFormDto.getNote()))
            content.append(String.format("Ghi chú: %s -> %s; ", rentalForm.getNote(), rentalFormDto.getNote()));
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phiếu thuê")
                .affectedObjectId(rentalForm.getId())
                .action(Action.UPDATE)
                .content(content.toString())
                .build();
        historyService.create(history);
        rentalFormMapper.updateEntityFromDto(rentalFormDto, rentalForm);
        rentalFormRepository.save(rentalForm);
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public void deleteRentalFormById(int id, int impactorId, String impactor) {
        RentalForm rentalForm=rentalFormRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        String content = String.format(
                "Phòng: %d; Nhân viên: %d; Ngày thuê: %s; Số ngày thuê: %d; Ghi chú: %s",
                rentalForm.getRoomId(),
                rentalForm.getStaffId(),
                rentalForm.getRentalDate(),
                rentalForm.getNumberOfRentalDays(),
                rentalForm.getNote()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phiếu thuê")
                .affectedObjectId(rentalForm.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        rentalFormRepository.delete(rentalForm);
    }

    @Transactional(readOnly = true)
    public List<Integer> getGuestIdByRentalFormId(int id) {
        RentalForm rentalForm = rentalFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        return rentalForm.getRentalFormDetails().stream().map(RentalFormDetail::getGuestId).toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseRentalFormDto> findByRoomIdAndRoomNameAndRentalFormId(
            Integer roomId, String roomName, Integer rfId) {
        List<RentalForm> rentalForms = rentalFormRepository.findByRoomIdAndRoomNameAndRentalFormId(roomId, roomName, rfId);
        return rentalFormMapper.toResponseDtoList(rentalForms);
    }

    @Transactional(readOnly = true)
    public List<ResponseRentalFormDto> searchUnpaid(SearchRentalFormDto searchRentalFormDto) {
        var lr = findByRoomIdAndRoomNameAndRentalFormId(
                searchRentalFormDto.getRoomId(),
                searchRentalFormDto.getRoomName(),
                searchRentalFormDto.getRentalFormId()
        );
        return lr.stream()
                .filter(rentalForm -> rentalForm.getIsPaidAt() == null)
                .toList();
    } //TODO: tạo endpooint cho phép tìm kiếm rental form chưa thanh toán, bổ sung lọc trùng trong frontend

    public int countTotalRentalDaysByRentalFormId(Integer id) {
        RentalForm rentalForm = rentalFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        var totalDays = rentalForm.getNumberOfRentalDays();
        for (var extensionForm : rentalForm.getRentalExtensionForms()) {
            totalDays += extensionForm.getNumberOfRentalDays();
        }
        return totalDays;
    }

    public double calculateTotalCostByRentalFormId(Integer id) {
        var rentalForm = rentalFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        return countTotalRentalDaysByRentalFormId(id) * rentalForm.getRoom().getRoomType().getPrice();
    }
}
