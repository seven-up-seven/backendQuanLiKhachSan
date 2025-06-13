package com.example.backendqlks.controller;

import com.example.backendqlks.dto.account.AccountDto;
import com.example.backendqlks.dto.staff.ResponseStaffDto;
import com.example.backendqlks.dto.staff.StaffDto;
import com.example.backendqlks.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<?> getAllStaffs(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            return ResponseEntity.ok(staffService.getAllStaffs(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching staff list: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable int id) {
        try {
            ResponseStaffDto staff = staffService.getStaffById(id);
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching staff with id: " + e.getMessage());
        }
    }

    @GetMapping("/account-id/{accountId}")
    public ResponseEntity<?> getStaffByAccountId(@PathVariable int accountId) {
        try {
            ResponseStaffDto staff=staffService.getStaffByAccountId(accountId);
            return ResponseEntity.ok(staff);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching staff with id: " + e.getMessage());
        }
    }

    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createStaff(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @RequestBody @Valid StaffDto staffDto,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseStaffDto createdStaff = staffService.createStaff(staffDto, impactorId, impactor);
            return ResponseEntity.ok(createdStaff);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating staff: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateStaff(@PathVariable int id,
                                         @PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @RequestBody @Valid StaffDto staffDto,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseStaffDto updatedStaff = staffService.updateStaff(id, staffDto, impactorId, impactor);
            return ResponseEntity.ok(updatedStaff);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating staff: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteStaff(@PathVariable int id,
                                         @PathVariable int impactorId,
                                         @PathVariable String impactor) {
        try {
            staffService.deleteStaffById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted staff with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting staff with id: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<?> getStaffSalary(@PathVariable int id) {
        try {
            double salary = staffService.getStaffSalary(id);
            return ResponseEntity.ok(salary);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching salary for staff with id: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/salary-multiplier/{impactorId}/{impactor}")
    public ResponseEntity<?> updateStaffSalaryMultiplier(@PathVariable int id,
                                                         @PathVariable int impactorId,
                                                         @PathVariable String impactor,
                                                         @RequestParam double salaryMultiplier) {
        try {
            if (salaryMultiplier < 0) {
                return ResponseEntity.badRequest().body("Salary multiplier must be greater than or equal to 0");
            }
            ResponseStaffDto updatedStaff = staffService.updateStaffSalaryMultiplier(id, salaryMultiplier, impactorId, impactor);
            return ResponseEntity.ok(updatedStaff);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating salary multiplier: " + e.getMessage());
        }
    }
}
