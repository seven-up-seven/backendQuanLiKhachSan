package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dao.StaffRepository;
import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.dto.account.ResponseAccountDto;
import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.mapper.AccountMapper;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final GuestRepository guestRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository,
                          AccountMapper accountMapper,
                          StaffRepository staffRepository,
                          GuestRepository guestRepository){
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.staffRepository = staffRepository;
        this.guestRepository = guestRepository;
    }
    @Transactional(readOnly = true)
    public ResponseAccountDto get(int accountId){
        var existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect account id"));
        return accountMapper.toResponseDto(existingAccount);
    }
    @Transactional(readOnly = true)
    public List<ResponseAccountDto> getAll(){
        var allAccount = accountRepository.findAll();
        return accountMapper.toResponseDtoList(allAccount);
    }
    //TODO: add password encoder BCryptPasswordEncoder, try catch
    public ResponseAccountDto create(AccountDto accountDto){
        if(accountRepository.existsByUsername(accountDto.getUserName())){
            throw new IllegalArgumentException("User name already exists");
        }
        var newAccount = accountMapper.toEntity(accountDto);
        accountRepository.save(newAccount);
        return accountMapper.toResponseDto(newAccount);
    }
    //TODO: add password encoder BCryptPasswordEncoder, try catch
    public ResponseAccountDto update(int accountId, AccountDto accountDto){
        var existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect account id"));
        accountMapper.updateEntityFromDto(accountDto, existingAccount);
        accountRepository.save(existingAccount);
        return accountMapper.toResponseDto(existingAccount);
    }

    public void delete(int accountId){
        var existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect account id"));
        Optional<Staff> staffUser;
        Optional<Guest> guestUser;
        staffUser = staffRepository.findByAccountId(accountId);
        guestUser = guestRepository.findByAccountId(accountId);
        if(staffUser.isEmpty() && guestUser.isEmpty()){
            throw new IllegalArgumentException("Incorrect account id");
        }
        staffUser.ifPresent(staff -> {
            staff.setAccountId(null);
            staffRepository.save(staff);
        });
        guestUser.ifPresent(guest -> {
            guest.setAccountId(null);
            guestRepository.save(guest);
        });
        accountRepository.delete(existingAccount);
    }
}

