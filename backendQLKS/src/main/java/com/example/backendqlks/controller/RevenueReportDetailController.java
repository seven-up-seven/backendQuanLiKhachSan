package com.example.backendqlks.controller;

import com.example.backendqlks.dto.revenuereportdetail.ResponseRevenueReportDetailDto;
import com.example.backendqlks.dto.revenuereportdetail.RevenueReportDetailDto;
import com.example.backendqlks.service.RevenueReportDetailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/revenue-report-detail")
public class RevenueReportDetailController {
    private final RevenueReportDetailService revenueReportDetailService;

    public RevenueReportDetailController(RevenueReportDetailService revenueReportDetailService) {
        this.revenueReportDetailService = revenueReportDetailService;
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @GetMapping
    public ResponseEntity<?> getAllRevenueReportDetails() {
        try {
            return ResponseEntity.ok(revenueReportDetailService.getAllRevenueReportDetails());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue report details: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRevenueReportDetailById(@PathVariable int id) {
        try {
            ResponseRevenueReportDetailDto revenueReportDetail = revenueReportDetailService.getRevenueReportDetailById(id);
            return ResponseEntity.ok(revenueReportDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue report detail with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createRevenueReportDetail(@PathVariable int impactorId,
                                                       @PathVariable String impactor,
                                                       @RequestBody @Valid RevenueReportDetailDto revenueReportDetailDto,
                                                       BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRevenueReportDetailDto createdRevenueReportDetail =
                    revenueReportDetailService.createRevenueReportDetail(revenueReportDetailDto, impactorId, impactor);
            return ResponseEntity.ok(createdRevenueReportDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating revenue report detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateRevenueReportDetail(@PathVariable int id,
                                                       @PathVariable int impactorId,
                                                       @PathVariable String impactor,
                                                       @RequestBody @Valid RevenueReportDetailDto revenueReportDetailDto,
                                                       BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRevenueReportDetailDto updatedRevenueReportDetail =
                    revenueReportDetailService.updateRevenueReportDetail(id, revenueReportDetailDto, impactorId, impactor);
            return ResponseEntity.ok(updatedRevenueReportDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating revenue report detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteRevenueReportDetail(@PathVariable int id,
                                                       @PathVariable int impactorId,
                                                       @PathVariable String impactor) {
        try {
            revenueReportDetailService.deleteRevenueReportDetailById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted revenue report detail with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting revenue report detail with id: " + e.getMessage());
        }
    }
}
