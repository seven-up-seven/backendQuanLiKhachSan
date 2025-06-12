package com.example.backendqlks.service;

import com.example.backendqlks.dao.BookingConfirmationFormRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.bookingConfirmationForm.BookingConfirmationFormDto;
import com.example.backendqlks.dto.bookingConfirmationForm.ResponseBookingConfirmationFormDto;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.BookingConfirmationFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BookingConfirmationFormService {
    private final BookingConfirmationFormRepository bookingConfirmationFormRepository;
    private final BookingConfirmationFormMapper bookingConfirmationFormMapper;
    private final RoomRepository roomRepository;

    public BookingConfirmationFormService(BookingConfirmationFormRepository bookingConfirmationFormRepository,
                                          BookingConfirmationFormMapper bookingConfirmationFormMapper, RoomRepository roomRepository){
        this.bookingConfirmationFormMapper = bookingConfirmationFormMapper;
        this.bookingConfirmationFormRepository = bookingConfirmationFormRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public ResponseBookingConfirmationFormDto get(int bookingConfirmationFormId){
        var existingBookingConfirmationForm = bookingConfirmationFormRepository.findById(bookingConfirmationFormId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect booking confirmation form id"));
        return bookingConfirmationFormMapper.toResponseDto(existingBookingConfirmationForm);
    }

    @Transactional(readOnly = true)
    public List<ResponseBookingConfirmationFormDto> getAll(){
        var allBookingConfirmationForm = bookingConfirmationFormRepository.findAll();
        return bookingConfirmationFormMapper.toResponseDtoList(allBookingConfirmationForm);
    }

    //TODO: add try catch
    public ResponseBookingConfirmationFormDto create(BookingConfirmationFormDto bookingConfirmationFormDto){
        var newBookingConfirmationForm = bookingConfirmationFormMapper.toEntity(bookingConfirmationFormDto);
        bookingConfirmationFormRepository.save(newBookingConfirmationForm);
        var room = roomRepository.findById(bookingConfirmationFormDto.getRoomId());
        if (room.isEmpty()) {
            throw new IllegalArgumentException("Room not found with id: " + bookingConfirmationFormDto.getRoomId());
        }
        room.get().setRoomState(RoomState.BOOKED);
        roomRepository.save(room.get());
        return bookingConfirmationFormMapper.toResponseDto(newBookingConfirmationForm);
    }
    //TODO: add try catch
    public ResponseBookingConfirmationFormDto update(int bookingConfirmationFormId, BookingConfirmationFormDto bookingConfirmationFormDto){
        var existingBookingConfirmationForm = bookingConfirmationFormRepository.findById(bookingConfirmationFormId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect booking confirmation form id"));
        bookingConfirmationFormMapper.updateEntityFromDto(bookingConfirmationFormDto, existingBookingConfirmationForm);
        bookingConfirmationFormRepository.save(existingBookingConfirmationForm);
        return bookingConfirmationFormMapper.toResponseDto(existingBookingConfirmationForm);
    }
    //TODO: modify later
    public void delete(int bookingConfirmationFormId){
        var existingBookingConfirmationForm = bookingConfirmationFormRepository.findById(bookingConfirmationFormId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect booking confirmation form id"));
        bookingConfirmationFormRepository.delete(existingBookingConfirmationForm);
    }
}
