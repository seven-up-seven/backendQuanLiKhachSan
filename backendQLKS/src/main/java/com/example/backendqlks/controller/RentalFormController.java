package com.example.backendqlks.controller;

import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.dto.rentalform.ResponseRentalFormDto;
import com.example.backendqlks.service.RentalFormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentalForm")
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
            if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
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
            if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
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
}
