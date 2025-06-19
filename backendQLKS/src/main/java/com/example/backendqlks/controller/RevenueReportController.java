package com.example.backendqlks.controller;

import com.example.backendqlks.dto.revenuereport.ResponseRevenueReportDto;
import com.example.backendqlks.dto.revenuereport.RevenueReportDto;
import com.example.backendqlks.service.RevenueReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/revenue-report")
public class RevenueReportController {
    private final RevenueReportService revenueReportService;

    public RevenueReportController(RevenueReportService revenueReportService) {
        this.revenueReportService = revenueReportService;
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @GetMapping
    public ResponseEntity<?> getAllRevenueReports() {
        try {
            return ResponseEntity.ok(revenueReportService.getAllRevenueReports());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue reports: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRevenueReportById(@PathVariable int id) {
        try {
            ResponseRevenueReportDto revenueReport = revenueReportService.getRevenueReportById(id);
            return ResponseEntity.ok(revenueReport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue report with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createRevenueReport(@PathVariable int impactorId,
                                                 @PathVariable String impactor,
                                                 @RequestBody @Valid RevenueReportDto revenueReportDto,
                                                 BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRevenueReportDto createdRevenueReport =
                    revenueReportService.createRevenueReport(revenueReportDto, impactorId, impactor);
            return ResponseEntity.ok(createdRevenueReport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating revenue report: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateRevenueReport(@PathVariable int id,
                                                 @PathVariable int impactorId,
                                                 @PathVariable String impactor,
                                                 @RequestBody @Valid RevenueReportDto revenueReportDto,
                                                 BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRevenueReportDto updatedRevenueReport =
                    revenueReportService.updateRevenueReport(id, revenueReportDto, impactorId, impactor);
            return ResponseEntity.ok(updatedRevenueReport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating revenue report: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNTANT')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteRevenueReport(@PathVariable int id,
                                                 @PathVariable int impactorId,
                                                 @PathVariable String impactor) {
        try {
            revenueReportService.deleteRevenueReportById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted revenue report with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting revenue report with id: " + e.getMessage());
        }
    }
}
