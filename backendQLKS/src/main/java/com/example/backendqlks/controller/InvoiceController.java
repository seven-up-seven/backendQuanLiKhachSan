package com.example.backendqlks.controller;

import com.example.backendqlks.dto.invoice.InvoiceDto;
import com.example.backendqlks.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable int id){
        try{
            return ResponseEntity.ok(invoiceService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoice: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllInvoices(){
        try{
            return ResponseEntity.ok(invoiceService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoices: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@Valid @RequestBody InvoiceDto invoiceDto,
                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdInvoice = invoiceService.create(invoiceDto);
            return ResponseEntity.ok(createdInvoice);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating invoice: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable int id,
                                           @Valid @RequestBody InvoiceDto invoiceDto,
                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedInvoice = invoiceService.update(id, invoiceDto);
            return ResponseEntity.ok(updatedInvoice);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating invoice: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable int id){
        try{
            invoiceService.delete(id);
            return ResponseEntity.ok("Invoice deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting invoice: " + e.getMessage());
        }
    }
}