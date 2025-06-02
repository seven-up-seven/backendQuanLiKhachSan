package com.example.backendqlks.controller;

import com.example.backendqlks.dto.invoiceDetail.InvoiceDetailDto;
import com.example.backendqlks.service.InvoiceDetailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/invoice-detail")
public class InvoiceDetailController {
    private final InvoiceDetailService invoiceDetailService;

    public InvoiceDetailController(InvoiceDetailService invoiceDetailService){
        this.invoiceDetailService = invoiceDetailService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceDetail(@PathVariable int id){
        try{
            return ResponseEntity.ok(invoiceDetailService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoice detail: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllInvoiceDetails(){
        try{
            return ResponseEntity.ok(invoiceDetailService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching invoice details: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createInvoiceDetail(@Valid @RequestBody InvoiceDetailDto invoiceDetailDto,
                                                 BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdInvoiceDetail = invoiceDetailService.create(invoiceDetailDto);
            return ResponseEntity.ok(createdInvoiceDetail);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating invoice detail: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoiceDetail(@PathVariable int id,
                                                 @Valid @RequestBody InvoiceDetailDto invoiceDetailDto,
                                                 BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedInvoiceDetail = invoiceDetailService.update(id, invoiceDetailDto);
            return ResponseEntity.ok(updatedInvoiceDetail);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating invoice detail: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoiceDetail(@PathVariable int id){
        try{
            invoiceDetailService.delete(id);
            return ResponseEntity.ok("Invoice detail deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting invoice detail: " + e.getMessage());
        }
    }
}