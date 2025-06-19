package com.example.backendqlks.controller;

import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.dto.rentalform.SearchRentalFormDto;
import com.example.backendqlks.service.RentalFormService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/rental-form")
public class RentalFormController {
    private final RentalFormService rentalFormService;

    public RentalFormController(RentalFormService rentalFormService) {
        this.rentalFormService = rentalFormService;
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/get-all-page")
    public ResponseEntity<?> getAllRentalForms(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            return ResponseEntity.ok(rentalFormService.getAllRentalFormPage(pageable));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental forms: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllRentalForms()
    {
        try {
            return ResponseEntity.ok(rentalFormService.getAllRentalForms());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental forms: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalFormById(@PathVariable int id) {
        try {
            ResponseRentalFormDto rentalForm = rentalFormService.getRentalFormById(id);
            return ResponseEntity.ok(rentalForm);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental form with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createRentalForm(@PathVariable int impactorId,
                                              @PathVariable String impactor,
                                              @RequestBody @Valid RentalFormDto rentalFormDto,
                                              BindingResult result) {
        try {
            if (result.hasErrors())
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());

            ResponseRentalFormDto createdRentalForm = rentalFormService.createRentalForm(rentalFormDto, impactorId, impactor);
            return ResponseEntity.ok(createdRentalForm);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental form: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateRentalForm(@PathVariable int id,
                                              @PathVariable int impactorId,
                                              @PathVariable String impactor,
                                              @RequestBody @Valid RentalFormDto rentalFormDto,
                                              BindingResult result) {
        try {
            if (result.hasErrors())
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());

            ResponseRentalFormDto updatedRentalForm = rentalFormService.updateRentalForm(id, rentalFormDto, impactorId, impactor);
            return ResponseEntity.ok(updatedRentalForm);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating rental form: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteRentalForm(@PathVariable int id,
                                              @PathVariable int impactorId,
                                              @PathVariable String impactor) {
        try {
            rentalFormService.deleteRentalFormById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted rental form with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting rental form with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @GetMapping("/{id}/guest-ids")
    public ResponseEntity<?> getGuestIdByRentalFormId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(rentalFormService.getGuestIdByRentalFormId(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guest IDs for rental form with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PostMapping("/search")
    public ResponseEntity<?> searchRentalForms(@RequestBody @Valid SearchRentalFormDto searchRentalFormDto) {
        try {
            return ResponseEntity.ok(rentalFormService.findByRoomIdAndRoomNameAndRentalFormId(searchRentalFormDto.getRoomId(),
                    searchRentalFormDto.getRoomName(), searchRentalFormDto.getRentalFormId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error searching rental forms: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PostMapping("/search-unpaid")
    public ResponseEntity<?> searchUnpaidRentalForms(@RequestBody @Valid SearchRentalFormDto searchRentalFormDto) {
        try {
            return ResponseEntity.ok(rentalFormService.searchUnpaid(searchRentalFormDto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error searching unpaid rental forms: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @GetMapping("/{id}/total-rental-days")
    public ResponseEntity<?> countTotalRentalDaysByRentalFormId(@PathVariable int id) {
        try {
            int totalRentalDays = rentalFormService.countTotalRentalDaysByRentalFormId(id);
            return ResponseEntity.ok(totalRentalDays);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error counting total rental days for rental form with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @GetMapping("/{id}/total-cost")
    public ResponseEntity<?> countTotalAmountByRentalFormId(@PathVariable int id) {
        try {
            double totalAmount = rentalFormService.calculateTotalCostByRentalFormId(id);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error counting total amount for rental form with id: " + e.getMessage());
        }
    }
}
