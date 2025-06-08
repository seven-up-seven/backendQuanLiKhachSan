package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dto.guest.GuestDto;
import com.example.backendqlks.dto.guest.ResponseGuestDto;
import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.mapper.GuestMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class GuestService {
    private final GuestRepository guestRepository;
    private final AccountRepository accountRepository;
    private final GuestMapper guestMapper;

    public GuestService(GuestRepository guestRepository,
                        GuestMapper guestMapper,
                        AccountRepository accountRepository){
        this.guestMapper = guestMapper;
        this.guestRepository = guestRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public ResponseGuestDto get(int guestId){
        var existingGuest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        return guestMapper.toResponseDto(existingGuest);
    }

    @Transactional(readOnly = true)
    public Page<ResponseGuestDto> getAll(Pageable pageable){
        Page<Guest> guestPage = guestRepository.findAll(pageable);
        List<ResponseGuestDto> guests=guestMapper.toResponseDtoList(guestPage.getContent());
        return new PageImpl<>(guests, pageable, guestPage.getTotalElements());
    }

    //TODO: add try catch
    public ResponseGuestDto create(GuestDto guestDto){
        if(guestRepository.findByIdentificationNumber(guestDto.getIdentificationNumber()).isPresent()){
            throw new IllegalArgumentException("Identification number already existed");
        }
        if(guestRepository.findByEmailContainingIgnoreCase(guestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already existed");
        }
        if(guestRepository.findByPhoneNumber(guestDto.getPhoneNumber()).isPresent()){
            throw new IllegalArgumentException("Phone number already existed");
        }
        var guest = guestMapper.toEntity(guestDto);
        guestRepository.save(guest);
        return guestMapper.toResponseDto(guest);
    }

    //TODO: add try catch
    public ResponseGuestDto update(int guestId, GuestDto guestDto){
        var existingGuest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        guestMapper.updateEntityFromDto(guestDto, existingGuest);
        guestRepository.save(existingGuest);
        return guestMapper.toResponseDto(existingGuest);
    }

    //TODO: lúc xoá guest thì xoá luôn account nếu có
    public void delete(int guestId){
        var existingGuest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        var accountId = existingGuest.getAccountId();
        if(accountId != null){
            accountRepository.deleteById(accountId);
        }
        guestRepository.deleteById(guestId);
    }

    public List<ResponseGuestDto> findByMultipleCriteria(Integer id, String identificationNumber, String email, String phoneNumber, Integer accountId) {
        var guest = guestRepository.findByMultipleCriteria(id, null, phoneNumber, identificationNumber, email, accountId);
        return guestMapper.toResponseDtoList(guest);
    }
}
