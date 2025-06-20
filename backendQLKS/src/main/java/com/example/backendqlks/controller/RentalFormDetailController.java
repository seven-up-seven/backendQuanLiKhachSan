package com.example.backendqlks.controller;

import com.example.backendqlks.dto.rentalformdetail.RentalFormDetailDto;
import com.example.backendqlks.dto.rentalformdetail.ResponseRentalFormDetailDto;
import com.example.backendqlks.service.RentalFormDetailService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/rental-form-detail")
public class RentalFormDetailController {
    private final RentalFormDetailService rentalFormDetailService;

    public RentalFormDetailController(RentalFormDetailService rentalFormDetailService) {
        this.rentalFormDetailService = rentalFormDetailService;
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllRentalFormDetails(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            return ResponseEntity.ok(rentalFormDetailService.getAllRentalFormDetails(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental form details: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalFormDetailById(@PathVariable int id) {
        try {
            ResponseRentalFormDetailDto rentalFormDetail = rentalFormDetailService.getRentalFormDetailById(id);
            return ResponseEntity.ok(rentalFormDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental form detail with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createRentalFormDetail(@PathVariable int impactorId,
                                                    @PathVariable String impactor,
                                                    @RequestBody @Valid RentalFormDetailDto rentalFormDetailDto,
                                                    BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRentalFormDetailDto createdRentalFormDetail =
                    rentalFormDetailService.createRentalFormDetail(rentalFormDetailDto, impactorId, impactor);
            return ResponseEntity.ok(createdRentalFormDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental form detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PostMapping("/rental-form/{rentalFormId}/{impactorId}/{impactor}")
    public ResponseEntity<?> createRentalFormDetails(@PathVariable int rentalFormId,
                                                     @PathVariable int impactorId,
                                                     @PathVariable String impactor,
                                                     @RequestBody List<Integer> guestIds,
                                                     BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdRentalFormDetails =
                    rentalFormDetailService.createRentalFormDetails(rentalFormId, guestIds, impactorId, impactor);
            return ResponseEntity.ok(createdRentalFormDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental form details: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateRentalFormDetail(@PathVariable int id,
                                                    @PathVariable int impactorId,
                                                    @PathVariable String impactor,
                                                    @RequestBody @Valid RentalFormDetailDto rentalFormDetailDto,
                                                    BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRentalFormDetailDto updatedRentalFormDetail =
                    rentalFormDetailService.updateRentalFormDetail(id, rentalFormDetailDto, impactorId, impactor);
            return ResponseEntity.ok(updatedRentalFormDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating rental form detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteRentalFormDetail(@PathVariable int id,
                                                    @PathVariable int impactorId,
                                                    @PathVariable String impactor) {
        try {
            rentalFormDetailService.deleteRentalFormDetailById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted rental form detail with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting rental form detail with id: " + e.getMessage());
        }
    }
}
