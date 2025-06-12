package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.GuestRepository;
import com.example.backendqlks.dao.StaffRepository;
import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.dto.account.ResponseAccountDto;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.entity.Guest;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.AccountMapper;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final GuestRepository guestRepository;
    private final AccountMapper accountMapper;
    private final HistoryService historyService;

    public AccountService(AccountRepository accountRepository,
                          AccountMapper accountMapper,
                          StaffRepository staffRepository,
                          GuestRepository guestRepository,
                          HistoryService historyService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.staffRepository = staffRepository;
        this.guestRepository = guestRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public ResponseAccountDto get(int accountId){
        var existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Can't find account with id: " + accountId));
        return accountMapper.toResponseDto(existingAccount);
    }

    @Transactional(readOnly = true)
    public ResponseAccountDto getByUsername(String username){
        var existingAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Can not find account with username " + username));
        return accountMapper.toResponseDto(existingAccount);
    }

    @Transactional(readOnly = true)
    public Page<ResponseAccountDto> getAll(Pageable pageable){
        var accountPage = accountRepository.findAll(pageable);
        List<ResponseAccountDto> accountDtos = accountMapper.toResponseDtoList(accountPage.getContent());
        return new PageImpl<>(accountDtos, pageable, accountPage.getTotalElements());
    }

    //TODO: add password encoder BCryptPasswordEncoder, try catch
    public ResponseAccountDto create(AccountDto accountDto, int impactorId, String impactor){
        if(accountRepository.existsByUsername(accountDto.getUserName())){
            throw new IllegalArgumentException("User name already exists");
        }
        var newAccount = accountMapper.toEntity(accountDto);
        accountRepository.save(newAccount);
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tài khoản")
                .affectedObjectId(newAccount.getId())
                .action(Action.CREATE)
                .content("username: "+accountDto.getUserName()+" password: " + accountDto.getPassWord())
                .build();
        historyService.create(history);
        return accountMapper.toResponseDto(newAccount);
    }

    //TODO: add password encoder BCryptPasswordEncoder, try catch
    public ResponseAccountDto update(int accountId, AccountDto accountDto, int impactorId, String impactor){
        var existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect account id"));
        String oldUsername = existingAccount.getUsername();
        String oldPassword = existingAccount.getPassword();
        accountMapper.updateEntityFromDto(accountDto, existingAccount);
        accountRepository.save(existingAccount);
        StringBuilder sb = new StringBuilder();
        if (!Objects.equals(oldUsername, accountDto.getUserName())) {
            sb.append("username: ")
                    .append(oldUsername)
                    .append(" -> ")
                    .append(accountDto.getUserName())
                    .append("; ");
        }
        if (!Objects.equals(oldPassword, accountDto.getPassWord())) {
            sb.append("password: ")
                    .append(oldPassword)
                    .append(" -> ")
                    .append(accountDto.getPassWord())
                    .append("; ");
        }
        String content = !sb.isEmpty()
                ? sb.substring(0, sb.length() - 2)
                : "no fields changed";
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tài khoản")
                .affectedObjectId(existingAccount.getId())
                .action(Action.UPDATE)
                .content(content)
                .build();
        historyService.create(history);
        return accountMapper.toResponseDto(existingAccount);
    }

    public void delete(int accountId, int impactorId, String impactor){
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
        HistoryDto history=HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Tài khoản")
                .affectedObjectId(existingAccount.getId())
                .action(Action.CREATE)
                .content("username: "+existingAccount.getUsername()+" password: " + existingAccount.getPassword())
                .build();
        historyService.create(history);
        accountRepository.delete(existingAccount);
    }
}

