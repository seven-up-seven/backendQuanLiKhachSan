package com.example.backendqlks.service;

import com.example.backendqlks.dao.RentalExtensionFormRepository;
import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.dto.rentalform.SearchRentalFormDto;
import com.example.backendqlks.entity.*;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.RentalFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RentalFormService {
    private final RentalFormRepository rentalFormRepository;
    private final RoomRepository roomRepository;
    private final RentalFormMapper rentalFormMapper;
    private final RentalExtensionFormRepository rentalExtensionFormRepository;

    public RentalFormService(RentalFormMapper rentalFormMapper,
                             RentalFormRepository rentalFormRepository,
                             RoomRepository roomRepository, RentalExtensionFormRepository rentalExtensionFormRepository) {
        this.rentalFormMapper = rentalFormMapper;
        this.rentalFormRepository = rentalFormRepository;
        this.roomRepository = roomRepository;
        this.rentalExtensionFormRepository = rentalExtensionFormRepository;
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
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public ResponseRentalFormDto updateRentalForm(int id, RentalFormDto rentalFormDto) {
        RentalForm rentalForm = rentalFormRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        rentalFormMapper.updateEntityFromDto(rentalFormDto, rentalForm);
        rentalFormRepository.save(rentalForm);
        return rentalFormMapper.toResponseDto(rentalForm);
    }

    public void deleteRentalFormById(int id) {
        RentalForm rentalForm=rentalFormRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        rentalFormRepository.delete(rentalForm);
    }

    public List<Integer> getGuestIdByRentalFormId(int id) {
        RentalForm rentalForm = rentalFormRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental Form With This Id Can Not Be Found"));
        return rentalForm.getRentalFormDetails().stream().map(RentalFormDetail::getGuestId).toList();
    }

    public List<ResponseRentalFormDto> findByRoomIdAndRoomNameAndRentalFormId(
            Integer roomId, String roomName, Integer rfId) {
        List<RentalForm> rentalForms = rentalFormRepository.findByRoomIdAndRoomNameAndRentalFormId(roomId, roomName, rfId);
        return rentalFormMapper.toResponseDtoList(rentalForms);
    }

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
