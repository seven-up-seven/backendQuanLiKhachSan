package com.example.backendqlks.controller;

import com.example.backendqlks.dto.invoice.InvoiceDto;
import com.example.backendqlks.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable int id){
        try{
            return ResponseEntity.ok(invoiceService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoice: " + e.getMessage());
        }
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST')")
    @GetMapping("/get-all-page")
    public ResponseEntity<?> getAllInvoices(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try{
            return ResponseEntity.ok(invoiceService.getAllPage(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoices: " + e.getMessage());
        }
    }

//    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST')")
    @GetMapping()
    public ResponseEntity<?> getAllInvoices() {
        try {
            return ResponseEntity.ok(invoiceService.getAll());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoices: " + e.getMessage());
        }
    }

    //@PreAuthorize("hasAnyAuthority('MANAGER', 'RECEPTIONIST', 'GUEST')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createInvoice(@PathVariable int impactorId,
                                           @PathVariable String impactor,
                                           @Valid @RequestBody InvoiceDto invoiceDto,
                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdInvoice = invoiceService.create(invoiceDto, impactorId, impactor);
            return ResponseEntity.ok(createdInvoice);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating invoice: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateInvoice(@PathVariable int id,
                                           @PathVariable int impactorId,
                                           @PathVariable String impactor,
                                           @Valid @RequestBody InvoiceDto invoiceDto,
                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedInvoice = invoiceService.update(id, invoiceDto, impactorId, impactor);
            return ResponseEntity.ok(updatedInvoice);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating invoice: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteInvoice(@PathVariable int id,
                                           @PathVariable int impactorId,
                                           @PathVariable String impactor) {
        try {
            invoiceService.delete(id, impactorId, impactor);
            return ResponseEntity.ok("Invoice deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting invoice: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST')")
    @GetMapping("/re-calculate/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> reCalculateInvoice(@PathVariable int id,
                                                @PathVariable int impactorId,
                                                @PathVariable String impactor) {
        try {
            var recalculatedInvoice = invoiceService.reCalculateInvoice(id, impactorId, impactor);
            return ResponseEntity.ok(recalculatedInvoice);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error recalculating invoice: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @GetMapping("/user-id/{userId}")
    public ResponseEntity<?> getInvoicesByUserId(@PathVariable int userId) {
        try {
            return ResponseEntity.ok(invoiceService.getAllInvoicesByUserId(userId));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoices: " + e.getMessage());
        }
    }

    @GetMapping("/today-money-amount")
    public ResponseEntity<?> getTodayMoneyAmount() {
        try {
            return ResponseEntity.ok(invoiceService.getTodayMoneyAmount());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching today's money amount: " + e.getMessage());
        }
    }
    
    //@PreAuthorize("hasAnyAuthority('MANAGER', 'ACCOUNTANT', 'RECEPTIONIST', 'GUEST')")
    @PostMapping("/send-email/{invoiceId}/{impactorId}/{impactor}")
    public ResponseEntity<?> sendEmailToGuests(@PathVariable int invoiceId,
                                               @PathVariable int impactorId,
                                               @PathVariable String impactor) {
        try {
            invoiceService.sendEmailAfterInvoicePayment(invoiceId);
            return ResponseEntity.ok("Đã gửi email hóa đơn cho khách thành công.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email thất bại: " + e.getMessage());
        }
    }
}