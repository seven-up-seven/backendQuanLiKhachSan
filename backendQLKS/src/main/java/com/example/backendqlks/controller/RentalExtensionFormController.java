package com.example.backendqlks.controller;

import com.example.backendqlks.dto.rentalextensionform.RentalExtensionFormDto;
import com.example.backendqlks.dto.rentalextensionform.ResponseRentalExtensionFormDto;
import com.example.backendqlks.dto.rentalform.RentalFormDto;
import com.example.backendqlks.entity.RentalExtensionForm;
import com.example.backendqlks.service.RentalExtensionFormService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

//    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping("/get-all-page")
    public ResponseEntity<?> getAllRentalExtensionForms(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            return ResponseEntity.ok(rentalExtensionFormService.getAllRentalExtensionFormPage(pageable));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental extension forms: "+e.getMessage());
        }
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllRentalExtensionForms() {
        try {
            return ResponseEntity.ok(rentalExtensionFormService.getAllRentalExtensionForms());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental extension forms: "+e.getMessage());
        }
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
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

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createRentalExtensionForm(@PathVariable int impactorId,
                                                       @PathVariable String impactor,
                                                       @RequestBody @Valid RentalExtensionFormDto rentalExtensionFormDto,
                                                       BindingResult result) {
        try {
            if (result.hasErrors())
                return ResponseEntity.status(400).body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());

            ResponseRentalExtensionFormDto createdRentalExtensionForm =
                    rentalExtensionFormService.createRentalExtensionForm(rentalExtensionFormDto, impactorId, impactor);

            return ResponseEntity.ok(createdRentalExtensionForm);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating rental extension form: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateRentalExtensionForm(@PathVariable int id,
                                                       @PathVariable int impactorId,
                                                       @PathVariable String impactor,
                                                       @RequestBody @Valid RentalExtensionFormDto rentalExtensionFormDto,
                                                       BindingResult result) {
        try {
            if (result.hasErrors())
                return ResponseEntity.status(400).body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());

            ResponseRentalExtensionFormDto updatedRentalExtensionForm =
                    rentalExtensionFormService.updateRentalExtensionForm(id, rentalExtensionFormDto, impactorId, impactor);

            return ResponseEntity.ok(updatedRentalExtensionForm);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating rental extension form: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteRentalExtensionForm(@PathVariable int id,
                                                       @PathVariable int impactorId,
                                                       @PathVariable String impactor) {
        try {
            rentalExtensionFormService.deleteRentalExtensionFormById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted Rental Extension Form with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting rental extension form: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @GetMapping("/day-remains/{rentalFormId}")
    public ResponseEntity<?> getDayRemains(@PathVariable int rentalFormId) {
        try {
            int dayRemains = rentalExtensionFormService.getDayRemains(rentalFormId);
            return ResponseEntity.ok(dayRemains);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching day remains: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'GUEST')")
    @GetMapping("/rental-form/{rentalFormId}")
    public ResponseEntity<?> getRentalExtensionFormByRentalFormId(@PathVariable int rentalFormId) {
        try {
            var responseDtos = rentalExtensionFormService.getRentalExtensionFormsByRentalFormId(rentalFormId);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rental extension form by rental form id: " + e.getMessage());
        }
    }
}
