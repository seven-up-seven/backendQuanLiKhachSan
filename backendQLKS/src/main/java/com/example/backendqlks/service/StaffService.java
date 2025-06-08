package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.StaffRepository;
import com.example.backendqlks.dto.staff.ResponseStaffDto;
import com.example.backendqlks.dto.staff.StaffDto;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.mapper.StaffMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final AccountRepository accountRepository;

    public StaffService(StaffMapper staffMapper, StaffRepository staffRepository, AccountRepository accountRepository) {
        this.staffMapper = staffMapper;
        this.staffRepository = staffRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseStaffDto> getAllStaffs(Pageable pageable) {
        Page<Staff> staffPage = staffRepository.findAll(pageable);
        List<ResponseStaffDto> staffs=staffMapper.toResponseDtoList(staffPage.getContent());
        return new PageImpl<>(staffs, pageable, staffPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ResponseStaffDto getStaffById(int id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        return staffMapper.toResponseDto(staff);
    }

    public ResponseStaffDto createStaff(StaffDto staffDto) {
        if (staffRepository.existsByIdentificationNumber(staffDto.getIdentificationNumber())) {
            throw new IllegalArgumentException("Staff with this email already exists");
        }
        Staff staff = staffMapper.toEntity(staffDto);
        staffRepository.save(staff);
        return staffMapper.toResponseDto(staff);
    }

    public ResponseStaffDto updateStaff(int id, StaffDto staffDto) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        staffMapper.updateEntityFromDto(staffDto, staff);
        staffRepository.save(staff);
        return staffMapper.toResponseDto(staff);
    }

    //delete related account if exists
    public void deleteStaffById(int id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        var accountId = staff.getAccountId();
        if(accountId != null) {
            var account = accountRepository.findById(accountId);
            if (account.isPresent()) {
                accountRepository.delete(account.get());
            } else {
                throw new IllegalArgumentException("Account with this ID cannot be found");
            }
        }
        staffRepository.delete(staff);
    }
}
