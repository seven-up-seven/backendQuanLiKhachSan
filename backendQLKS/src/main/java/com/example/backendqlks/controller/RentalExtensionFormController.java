package com.example.backendqlks.controller;

import com.example.backendqlks.dto.rentalextensionform.RentalExtensionFormDto;
import com.example.backendqlks.dto.rentalextensionform.ResponseRentalExtensionFormDto;
import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.service.RentalExtensionFormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/rental-extension-form")
public class RentalExtensionFormController {
    private final RentalExtensionFormService rentalExtensionFormService;

    public RentalExtensionFormController(RentalExtensionFormService rentalExtensionFormService) {
        this.rentalExtensionFormService = rentalExtensionFormService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRentalExtensionForms() {
        try {
            return ResponseEntity.ok(rentalExtensionFormService.getAllRentalExtensionForms());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental extension forms: "+e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalExtensionFormById(@PathVariable int id) {
        try {
            ResponseRentalExtensionFormDto rentalExtensionForm=rentalExtensionFormService.getRentalExtensionFormById(id);
            return ResponseEntity.ok(rentalExtensionForm);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental extension form with id: "+e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRentalExtensionForm(@RequestBody @Valid RentalExtensionFormDto rentalExtensionFormDto, BindingResult result) {
        try {
            if (result.hasErrors()) return ResponseEntity.status(500).body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            ResponseRentalExtensionFormDto createdRentalExtensionForm=rentalExtensionFormService.createRentalExtensionForm(rentalExtensionFormDto);
            return ResponseEntity.ok(createdRentalExtensionForm);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental extension form: "+e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRentalExtensionForm(@RequestBody @Valid RentalExtensionFormDto rentalExtensionFormDto,
                                                       @PathVariable int id,
                                                       BindingResult result) {
        try {
            if (result.hasErrors()) return ResponseEntity.status(500).body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            ResponseRentalExtensionFormDto updatedRentalExtensionForm=rentalExtensionFormService.updateRentalExtensionForm(id, rentalExtensionFormDto);
            return ResponseEntity.ok(updatedRentalExtensionForm);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating rental extension form: "+e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRentalExtensionForm(@PathVariable int id) {
        try {
            rentalExtensionFormService.deleteRentalExtensionFormById(id);
            return ResponseEntity.ok("Deleted Rental Extension Form with id: "+id);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting rental extension form with id: "+e.getMessage());
        }
    }

    @GetMapping("/day-remains/{rentalFormId}")
    public ResponseEntity<?> getDayRemains(@PathVariable int rentalFormId) {
        try {
            int dayRemains = rentalExtensionFormService.getDayRemains(rentalFormId);
            return ResponseEntity.ok(dayRemains);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching day remains: " + e.getMessage());
        }
    }
}
