package com.example.backendqlks.service;

import com.example.backendqlks.dao.StaffRepository;
import com.example.backendqlks.dto.staff.ResponseStaffDto;
import com.example.backendqlks.dto.staff.StaffDto;
import com.example.backendqlks.entity.Staff;
import com.example.backendqlks.mapper.StaffMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;

    public StaffService(StaffMapper staffMapper, StaffRepository staffRepository) {
        this.staffMapper = staffMapper;
        this.staffRepository = staffRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseStaffDto> getAllStaffs() {
        List<Staff> staffs = staffRepository.findAll();
        return staffMapper.toResponseDtoList(staffs);
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

    public void deleteStaffById(int id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff with this ID cannot be found"));
        staffRepository.delete(staff);
    }
}
