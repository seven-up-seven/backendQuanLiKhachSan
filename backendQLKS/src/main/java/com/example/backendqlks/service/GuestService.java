package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dto.account.ResponseAccountDto;
import com.example.backendqlks.dto.guest.GuestDto;
import com.example.backendqlks.dto.guest.ResponseGuestDto;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.GuestMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class GuestService {
    private final GuestRepository guestRepository;
    private final AccountRepository accountRepository;
    private final GuestMapper guestMapper;
    private final HistoryService historyService;

    public GuestService(GuestRepository guestRepository,
                        GuestMapper guestMapper,
                        AccountRepository accountRepository,
                        HistoryService historyService) {
        this.guestMapper = guestMapper;
        this.guestRepository = guestRepository;
        this.accountRepository = accountRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public ResponseGuestDto get(int guestId){
        var existingGuest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        return guestMapper.toResponseDto(existingGuest);
    }

    @Transactional(readOnly = true)
    public Page<ResponseGuestDto> getAllPage(Pageable pageable){
        Page<Guest> guestPage = guestRepository.findAll(pageable);
        List<ResponseGuestDto> guests=guestMapper.toResponseDtoList(guestPage.getContent());
        return new PageImpl<>(guests, pageable, guestPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseGuestDto> getAll()
    {
        var result=guestRepository.findAll();
        return guestMapper.toResponseDtoList(result);
    }

    @Transactional(readOnly = true)
    public ResponseGuestDto getGuestByAccountId(int accountId){
        var result=guestRepository.findByAccountId(accountId).orElseThrow(() -> new IllegalArgumentException("Incorrect account id"));
        return guestMapper.toResponseDto(result);
    }

    @Transactional(readOnly = true)
    public Page<ResponseGuestDto> getGuestsWithoutAccount(Pageable pageable){
        var result=guestRepository.findByAccountIdIsNull(pageable);
        List<ResponseGuestDto> guests=guestMapper.toResponseDtoList(result.getContent());
        return new PageImpl<>(guests, pageable, result.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseGuestDto> getGuestWithName(String name) {
        var result=guestRepository.findGuestsByNameContainingIgnoreCase(name);
        return guestMapper.toResponseDtoList(result);
    }

    //TODO: add try catch
    public ResponseGuestDto create(GuestDto guestDto, int impactorId, String impactor) {
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
        String content = String.format(
                "Tên: %s; Giới tính: %s; Tuổi: %d; CMND/CCCD: %s; SĐT: %s; Email: %s",
                guestDto.getName(),
                guestDto.getSex(),
                guestDto.getAge(),
                guestDto.getIdentificationNumber(),
                guestDto.getPhoneNumber(),
                guestDto.getEmail()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Khách")
                .affectedObjectId(guest.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return guestMapper.toResponseDto(guest);
    }

    //TODO: add try catch
    public ResponseGuestDto update(int guestId, GuestDto guestDto, int impactorId, String impactor) {
        var existingGuest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        List<String> changes = new ArrayList<>();
        if (!Objects.equals(existingGuest.getName(), guestDto.getName())) {
            changes.add("Tên: " + existingGuest.getName() + " → " + guestDto.getName());
        }
        if (!Objects.equals(existingGuest.getSex(), guestDto.getSex())) {
            changes.add("Giới tính: " + existingGuest.getSex() + " → " + guestDto.getSex());
        }
        if (!Objects.equals(existingGuest.getAge(), guestDto.getAge())) {
            changes.add("Tuổi: " + existingGuest.getAge() + " → " + guestDto.getAge());
        }
        if (!Objects.equals(existingGuest.getIdentificationNumber(), guestDto.getIdentificationNumber())) {
            changes.add("CMND/CCCD: " + existingGuest.getIdentificationNumber() + " → " + guestDto.getIdentificationNumber());
        }
        if (!Objects.equals(existingGuest.getPhoneNumber(), guestDto.getPhoneNumber())) {
            changes.add("SĐT: " + existingGuest.getPhoneNumber() + " → " + guestDto.getPhoneNumber());
        }
        if (!Objects.equals(existingGuest.getEmail(), guestDto.getEmail())) {
            changes.add("Email: " + existingGuest.getEmail() + " → " + guestDto.getEmail());
        }
        if (!Objects.equals(existingGuest.getAccountId(), guestDto.getAccountId())) {
            changes.add("Account ID: " + existingGuest.getAccountId() + " → " + guestDto.getAccountId());
        }
        String content = changes.isEmpty() ? "Không có thay đổi." : String.join("; ", changes);
        guestMapper.updateEntityFromDto(guestDto, existingGuest);
        guestRepository.save(existingGuest);
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Khách")
                .affectedObjectId(existingGuest.getId())
                .action(Action.UPDATE)
                .content(content)
                .build();
        historyService.create(history);
        return guestMapper.toResponseDto(existingGuest);
    }

    public void delete(int guestId, int impactorId, String impactor){
        var existingGuest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect floor id"));
        var accountId = existingGuest.getAccountId();
        if(accountId != null){
            accountRepository.deleteById(accountId);
        }
        String content = String.format(
                "Tên: %s; Giới tính: %s; Tuổi: %d; CMND/CCCD: %s; SĐT: %s; Email: %s",
                existingGuest.getName(),
                existingGuest.getSex(),
                existingGuest.getAge(),
                existingGuest.getIdentificationNumber(),
                existingGuest.getPhoneNumber(),
                existingGuest.getEmail()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Khách")
                .affectedObjectId(existingGuest.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        guestRepository.deleteById(guestId);
    }

    public List<ResponseGuestDto> findByMultipleCriteria(Integer id, String name ,String identificationNumber, String email, String phoneNumber, Integer accountId) {
        var guest = guestRepository.findByMultipleCriteria(id, name, phoneNumber, identificationNumber, email, accountId);
        return guestMapper.toResponseDtoList(guest);
    }
}
