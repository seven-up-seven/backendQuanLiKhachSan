package com.example.backendqlks.controller;

import com.example.backendqlks.dto.rentalformdetail.RentalFormDetailDto;
import com.example.backendqlks.dto.rentalformdetail.ResponseRentalFormDetailDto;
import com.example.backendqlks.service.RentalFormDetailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/rentalFormDetail")
public class RentalFormDetailController {
    private final RentalFormDetailService rentalFormDetailService;

    public RentalFormDetailController(RentalFormDetailService rentalFormDetailService) {
        this.rentalFormDetailService = rentalFormDetailService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRentalFormDetails() {
        try {
            return ResponseEntity.ok(rentalFormDetailService.getAllRentalFormDetails());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental form details: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalFormDetailById(@PathVariable int id) {
        try {
            ResponseRentalFormDetailDto rentalFormDetail = rentalFormDetailService.getRentalFormDetailById(id);
            return ResponseEntity.ok(rentalFormDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental form detail with id: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRentalFormDetail(@RequestBody @Valid RentalFormDetailDto rentalFormDetailDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRentalFormDetailDto createdRentalFormDetail = rentalFormDetailService.createRentalFormDetail(rentalFormDetailDto);
            return ResponseEntity.ok(createdRentalFormDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental form detail: " + e.getMessage());
        }
    }

    @PostMapping("/rental-form/{rentalFormId}")
    public ResponseEntity<?> createRentalFormDetails(@PathVariable int rentalFormId, @RequestBody List<Integer> guestIds, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdRentalFormDetails = rentalFormDetailService.createRentalFormDetails(rentalFormId, guestIds);
            return ResponseEntity.ok(createdRentalFormDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental form details: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRentalFormDetail(@PathVariable int id, @RequestBody @Valid RentalFormDetailDto rentalFormDetailDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRentalFormDetailDto updatedRentalFormDetail = rentalFormDetailService.updateRentalFormDetail(id, rentalFormDetailDto);
            return ResponseEntity.ok(updatedRentalFormDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating rental form detail: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRentalFormDetail(@PathVariable int id) {
        try {
            rentalFormDetailService.deleteRentalFormDetailById(id);
            return ResponseEntity.ok("Deleted rental form detail with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting rental form detail with id: " + e.getMessage());
        }
    }
}
