package com.example.backendqlks.controller;

import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.dto.rentalform.SearchRentalFormDto;
import com.example.backendqlks.service.RentalFormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<?> getAllRentalForms() {
        try {
            return ResponseEntity.ok(rentalFormService.getAllRentalForms());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental forms: " + e.getMessage());
        }
    }

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

    @PostMapping
    public ResponseEntity<?> createRentalForm(@RequestBody @Valid RentalFormDto rentalFormDto, BindingResult result) {
        try {
            if (result.hasErrors()) return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            ResponseRentalFormDto createdRentalForm = rentalFormService.createRentalForm(rentalFormDto);
            return ResponseEntity.ok(createdRentalForm);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental form: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRentalForm(@RequestBody @Valid RentalFormDto rentalFormDto,
                                              @PathVariable int id,
                                              BindingResult result) {
        try {
            if (result.hasErrors()) return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            ResponseRentalFormDto updatedRentalForm=rentalFormService.updateRentalForm(id, rentalFormDto);
            return ResponseEntity.ok(updatedRentalForm);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating rental form: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRentalForm(@PathVariable int id) {
        try {
            rentalFormService.deleteRentalFormById(id);
            return ResponseEntity.ok("Deleted rental form with id: " + id);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting rental form with id: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/guest-ids")
    public ResponseEntity<?> getGuestIdByRentalFormId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(rentalFormService.getGuestIdByRentalFormId(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guest IDs for rental form with id: " + e.getMessage());
        }
    }
    @PostMapping("/search")
    public ResponseEntity<?> searchRentalForms(@RequestBody @Valid SearchRentalFormDto searchRentalFormDto) {
        try {
            return ResponseEntity.ok(rentalFormService.findByRoomIdAndRoomNameAndRentalFormId(searchRentalFormDto.getRoomId(),
                    searchRentalFormDto.getRoomName(), searchRentalFormDto.getRentalFormId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error searching rental forms: " + e.getMessage());
        }
    }
    @PostMapping("/search-unpaid")
    public ResponseEntity<?> searchUnpaidRentalForms(@RequestBody @Valid SearchRentalFormDto searchRentalFormDto) {
        try {
            return ResponseEntity.ok(rentalFormService.searchUnpaid(searchRentalFormDto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error searching unpaid rental forms: " + e.getMessage());
        }
    }
    @GetMapping("/{id}/total-rental-days")
    public ResponseEntity<?> countTotalRentalDaysByRentalFormId(@PathVariable int id) {
        try {
            int totalRentalDays = rentalFormService.countTotalRentalDaysByRentalFormId(id);
            return ResponseEntity.ok(totalRentalDays);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error counting total rental days for rental form with id: " + e.getMessage());
        }
    }

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
