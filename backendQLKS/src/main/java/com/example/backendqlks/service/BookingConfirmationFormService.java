package com.example.backendqlks.service;

import com.example.backendqlks.dao.BookingConfirmationFormRepository;
import com.example.backendqlks.dto.bookingConfirmationForm.BookingConfirmationFormDto;
import com.example.backendqlks.dto.bookingConfirmationForm.ResponseBookingConfirmationFormDto;
import com.example.backendqlks.mapper.BookingConfirmationFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BookingConfirmationFormService {
    private final BookingConfirmationFormRepository bookingConfirmationFormRepository;
    private final BookingConfirmationFormMapper bookingConfirmationFormMapper;

    public BookingConfirmationFormService(BookingConfirmationFormRepository bookingConfirmationFormRepository,
                                          BookingConfirmationFormMapper bookingConfirmationFormMapper){
        this.bookingConfirmationFormMapper = bookingConfirmationFormMapper;
        this.bookingConfirmationFormRepository = bookingConfirmationFormRepository;
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
