package com.example.backendqlks.service;

import com.example.backendqlks.dao.RentalFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.entity.Invoice;
import com.example.backendqlks.entity.InvoiceDetail;
import com.example.backendqlks.entity.RentalForm;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.RentalFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RentalFormService {
    private final RentalFormRepository rentalFormRepository;
    private final RoomRepository roomRepository;
    private final RentalFormMapper rentalFormMapper;

    public RentalFormService(RentalFormMapper rentalFormMapper,
                             RentalFormRepository rentalFormRepository,
                             RoomRepository roomRepository) {
        this.rentalFormMapper = rentalFormMapper;
        this.rentalFormRepository = rentalFormRepository;
        this.roomRepository = roomRepository;
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
}
