package com.example.backendqlks.controller;

import com.example.backendqlks.dto.revenuereportdetail.ResponseRevenueReportDetailDto;
import com.example.backendqlks.dto.revenuereportdetail.RevenueReportDetailDto;
import com.example.backendqlks.service.RevenueReportDetailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/revenueReportDetail")
public class RevenueReportDetailController {
    private final RevenueReportDetailService revenueReportDetailService;

    public RevenueReportDetailController(RevenueReportDetailService revenueReportDetailService) {
        this.revenueReportDetailService = revenueReportDetailService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRevenueReportDetails() {
        try {
            return ResponseEntity.ok(revenueReportDetailService.getAllRevenueReportDetails());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue report details: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRevenueReportDetailById(@PathVariable int id) {
        try {
            ResponseRevenueReportDetailDto revenueReportDetail = revenueReportDetailService.getRevenueReportDetailById(id);
            return ResponseEntity.ok(revenueReportDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue report detail with id: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRevenueReportDetail(@RequestBody @Valid RevenueReportDetailDto revenueReportDetailDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseRevenueReportDetailDto createdRevenueReportDetail = revenueReportDetailService.createRevenueReportDetail(revenueReportDetailDto);
            return ResponseEntity.ok(createdRevenueReportDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating revenue report detail: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRevenueReportDetail(@PathVariable int id, @RequestBody @Valid RevenueReportDetailDto revenueReportDetailDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseRevenueReportDetailDto updatedRevenueReportDetail = revenueReportDetailService.updateRevenueReportDetail(id, revenueReportDetailDto);
            return ResponseEntity.ok(updatedRevenueReportDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating revenue report detail: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRevenueReportDetail(@PathVariable int id) {
        try {
            revenueReportDetailService.deleteRevenueReportDetailById(id);
            return ResponseEntity.ok("Deleted revenue report detail with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting revenue report detail with id: " + e.getMessage());
        }
    }
}
