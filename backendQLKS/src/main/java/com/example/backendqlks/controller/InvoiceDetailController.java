package com.example.backendqlks.controller;

import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.service.InvoiceDetailService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/invoice-detail")
public class InvoiceDetailController {
    private final InvoiceDetailService invoiceDetailService;

    public InvoiceDetailController(InvoiceDetailService invoiceDetailService){
        this.invoiceDetailService = invoiceDetailService;
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceDetail(@PathVariable int id){
        try{
            return ResponseEntity.ok(invoiceDetailService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoice detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST')")
    @GetMapping
    public ResponseEntity<?> getAllInvoiceDetails() {
        try{
            var invoiceDetails = invoiceDetailService.getAll();
            return ResponseEntity.ok(invoiceDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoice details: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST')")
    @GetMapping("/page")
    public ResponseEntity<?> getAllInvoiceDetails(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try{
            return ResponseEntity.ok(invoiceDetailService.getAllPage(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoice details: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createInvoiceDetail(@PathVariable int impactorId,
                                                 @PathVariable String impactor,
                                                 @Valid @RequestBody InvoiceDetailDto invoiceDetailDto,
                                                 BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdInvoiceDetail = invoiceDetailService.create(invoiceDetailDto, impactorId, impactor);
            return ResponseEntity.ok(createdInvoiceDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating invoice detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateInvoiceDetail(@PathVariable int id,
                                                 @PathVariable int impactorId,
                                                 @PathVariable String impactor,
                                                 @Valid @RequestBody InvoiceDetailDto invoiceDetailDto,
                                                 BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedInvoiceDetail = invoiceDetailService.update(id, invoiceDetailDto, impactorId, impactor);
            return ResponseEntity.ok(updatedInvoiceDetail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating invoice detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteInvoiceDetail(@PathVariable int id,
                                                 @PathVariable int impactorId,
                                                 @PathVariable String impactor) {
        try {
            invoiceDetailService.delete(id, impactorId, impactor);
            return ResponseEntity.ok("Invoice detail deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting invoice detail: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @PostMapping("/invoice/{invoiceId}/{impactorId}/{impactor}")
    public ResponseEntity<?> createInvoiceDetailForInvoice(@PathVariable int invoiceId,
                                                           @PathVariable int impactorId,
                                                           @PathVariable String impactor,
                                                           @Valid @RequestBody List<Integer> rentalFormIds,
                                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdInvoiceDetails = invoiceDetailService.createInvoiceDetails(invoiceId, rentalFormIds, impactorId, impactor);
            return ResponseEntity.ok(createdInvoiceDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating invoice details for invoice: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @PostMapping("/checkout/{invoiceId}/{rentalFormId}/{impactorId}/{impactor}")
    public ResponseEntity<?> checkoutInvoiceDetail(@PathVariable int invoiceId,
                                                   @PathVariable int rentalFormId,
                                                   @PathVariable int impactorId,
                                                   @PathVariable String impactor) {
        try {
            var checkoutResult = invoiceDetailService.checkOut(invoiceId, rentalFormId, impactorId, impactor);
            return ResponseEntity.ok(checkoutResult);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error checking out invoice detail: " + e.getMessage());
        }
    }
}
