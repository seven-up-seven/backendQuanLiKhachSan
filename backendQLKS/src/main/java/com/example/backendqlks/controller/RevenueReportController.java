package com.example.backendqlks.controller;

import com.example.backendqlks.dto.revenuereport.ResponseRevenueReportDto;
import com.example.backendqlks.dto.revenuereport.RevenueReportDto;
import com.example.backendqlks.service.RevenueReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/revenueReport")
public class RevenueReportController {
    private final RevenueReportService revenueReportService;

    public RevenueReportController(RevenueReportService revenueReportService) {
        this.revenueReportService = revenueReportService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRevenueReports() {
        try {
            return ResponseEntity.ok(revenueReportService.getAllRevenueReports());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue reports: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRevenueReportById(@PathVariable int id) {
        try {
            ResponseRevenueReportDto revenueReport = revenueReportService.getRevenueReportById(id);
            return ResponseEntity.ok(revenueReport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue report with id: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRevenueReport(@RequestBody @Valid RevenueReportDto revenueReportDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseRevenueReportDto createdRevenueReport = revenueReportService.createRevenueReport(revenueReportDto);
            return ResponseEntity.ok(createdRevenueReport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating revenue report: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRevenueReport(@PathVariable int id, @RequestBody @Valid RevenueReportDto revenueReportDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseRevenueReportDto updatedRevenueReport = revenueReportService.updateRevenueReport(id, revenueReportDto);
            return ResponseEntity.ok(updatedRevenueReport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating revenue report: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRevenueReport(@PathVariable int id) {
        try {
            revenueReportService.deleteRevenueReportById(id);
            return ResponseEntity.ok("Deleted revenue report with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting revenue report with id: " + e.getMessage());
        }
    }
}
