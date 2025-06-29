package com.example.backendqlks.service;

import com.example.backendqlks.dao.AccountRepository;
import com.example.backendqlks.dao.PositionRepository;
import com.example.backendqlks.dao.StaffRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.staff.ResponseStaffDto;
import com.example.backendqlks.dto.staff.StaffDto;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.StaffMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final AccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final HistoryService historyService;

    public StaffService(StaffMapper staffMapper,
                        StaffRepository staffRepository,
                        AccountRepository accountRepository,
                        PositionRepository positionRepository,
                        HistoryService historyService) {
        this.staffMapper = staffMapper;
        this.staffRepository = staffRepository;
        this.accountRepository = accountRepository;
        this.positionRepository = positionRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public Page<ResponseStaffDto> getAllStaffPage(Pageable pageable) {
        Page<Staff> staffPage = staffRepository.findAll(pageable);
        List<ResponseStaffDto> staffs=staffMapper.toResponseDtoList(staffPage.getContent());
        return new PageImpl<>(staffs, pageable, staffPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseStaffDto> getAllStaff() {
        return staffMapper.toResponseDtoList(staffRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ResponseStaffDto getStaffById(int id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        return staffMapper.toResponseDto(staff);
    }

    @Transactional(readOnly = true)
    public ResponseStaffDto getStaffByAccountId(int accountId) {
        Staff staff=staffRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        return staffMapper.toResponseDto(staff);
    }

    @Transactional(readOnly = true)
    public Page<ResponseStaffDto> getStaffWithNoAccount(Pageable pageable) {
        Page<Staff> staffPage=staffRepository.findByAccountIdIsNull(pageable);
        List<ResponseStaffDto> staffs=staffMapper.toResponseDtoList(staffPage.getContent());
        return new PageImpl<>(staffs, pageable, staffPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<ResponseStaffDto> getStaffWithName(String name) {
        var result=staffRepository.findByFullNameContainingIgnoreCase(name);
        return staffMapper.toResponseDtoList(result);
    }

    public ResponseStaffDto createStaff(StaffDto staffDto, int impactorId, String impactor) {
        if (staffRepository.existsByIdentificationNumber(staffDto.getIdentificationNumber())) {
            throw new IllegalArgumentException("Staff with this email already exists");
        }
        Staff staff = staffMapper.toEntity(staffDto);
        staff.setSalaryMultiplier(1.0f); // Default salary multiplier
        staffRepository.save(staff);
        String content = String.format(
                "Tên: %s; Giới tính: %s; Tuổi: %d; CCCD: %s; Địa chỉ: %s; Hệ số lương: %.2f; ID chức vụ: %d; ID tài khoản: %s",
                staff.getFullName(), staff.getSex(), staff.getAge(), staff.getIdentificationNumber(),
                staff.getAddress(), staff.getSalaryMultiplier(), staff.getPositionId(),
                staff.getAccountId() != null ? staff.getAccountId().toString() : "null"
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Nhân viên")
                .affectedObjectId(staff.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return staffMapper.toResponseDto(staff);
    }

    public ResponseStaffDto updateStaff(int id, StaffDto staffDto, int impactorId, String impactor) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        StringBuilder contentBuilder = new StringBuilder();
        Staff oldStaff = staffRepository.findById(id).get();
        if (!Objects.equals(oldStaff.getFullName(), staffDto.getFullName()))
            contentBuilder.append(String.format("Tên: %s -> %s; ", oldStaff.getFullName(), staffDto.getFullName()));
        if (!Objects.equals(oldStaff.getAge(), staffDto.getAge()))
            contentBuilder.append(String.format("Tuổi: %d -> %d; ", oldStaff.getAge(), staffDto.getAge()));
        if (!Objects.equals(oldStaff.getIdentificationNumber(), staffDto.getIdentificationNumber()))
            contentBuilder.append(String.format("CCCD: %s -> %s; ", oldStaff.getIdentificationNumber(), staffDto.getIdentificationNumber()));
        if (!Objects.equals(oldStaff.getAddress(), staffDto.getAddress()))
            contentBuilder.append(String.format("Địa chỉ: %s -> %s; ", oldStaff.getAddress(), staffDto.getAddress()));
        if (!Objects.equals(oldStaff.getSex(), staffDto.getSex()))
            contentBuilder.append(String.format("Giới tính: %s -> %s; ", oldStaff.getSex(), staffDto.getSex()));
        if (!Objects.equals(oldStaff.getPositionId(), staffDto.getPositionId()))
            contentBuilder.append(String.format("Chức vụ ID: %d -> %d; ", oldStaff.getPositionId(), staffDto.getPositionId()));
        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Nhân viên")
                    .affectedObjectId(id)
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }
        staffMapper.updateEntityFromDto(staffDto, staff);
        staffRepository.save(staff);
        return staffMapper.toResponseDto(staff);
    }

    //delete related account if exists
    public void deleteStaffById(int id, int impactorId, String impactor) {
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
        String content = String.format(
                "Tên: %s; Giới tính: %s; Tuổi: %d; CCCD: %s; Địa chỉ: %s; Hệ số lương: %.2f; ID chức vụ: %d; ID tài khoản: %s",
                staff.getFullName(), staff.getSex(), staff.getAge(), staff.getIdentificationNumber(),
                staff.getAddress(), staff.getSalaryMultiplier(), staff.getPositionId(),
                staff.getAccountId() != null ? staff.getAccountId().toString() : "null"
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Nhân viên")
                .affectedObjectId(staff.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        staffRepository.delete(staff);
    }

    public ResponseStaffDto updateStaffSalaryMultiplier(int id, double salaryMultiplier, int impactorId, String impactor) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        if (salaryMultiplier < 0) {
            throw new IllegalArgumentException("Salary multiplier must be greater than or equal to 0");
        }
        String content = String.format(
                "Hệ số lương: %.2f -> %.2f", staff.getSalaryMultiplier(), salaryMultiplier
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Nhân viên")
                .affectedObjectId(staff.getId())
                .action(Action.UPDATE)
                .content(content)
                .build();
        historyService.create(history);
        staff.setSalaryMultiplier((float) salaryMultiplier);
        staffRepository.save(staff);
        return staffMapper.toResponseDto(staff);
    }

    public double getStaffSalary(int id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        return staff.getSalaryMultiplier() * staff.getPosition().getBaseSalary();
    }

    public int getStaffAmount() {
        return (int) staffRepository.count();
    }
}
