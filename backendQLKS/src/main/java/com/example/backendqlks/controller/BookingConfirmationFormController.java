package com.example.backendqlks.controller;

import com.example.backendqlks.dto.bookingConfirmationForm.BookingConfirmationFormDto;
import com.example.backendqlks.service.BookingConfirmationFormService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/booking-confirmation-form")
public class BookingConfirmationFormController {
    private final BookingConfirmationFormService bookingConfirmationFormService;

    public BookingConfirmationFormController(BookingConfirmationFormService bookingConfirmationFormService){
        this.bookingConfirmationFormService = bookingConfirmationFormService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingConfirmationForm(@PathVariable int id){
        try{
            return ResponseEntity.ok(bookingConfirmationFormService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching booking confirmation form: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllBookingConfirmationForms(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try{
            return ResponseEntity.ok(bookingConfirmationFormService.getAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching booking confirmation forms: " + e.getMessage());
        }
    }

    @GetMapping("/list-id/{ids}")
    public ResponseEntity<?> getByListId(@PathVariable List<Integer> ids) {
        try {
            return ResponseEntity.ok(bookingConfirmationFormService.getByListIds(ids));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching booking confirmation form: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBookingConfirmationForm(@Valid @RequestBody BookingConfirmationFormDto bookingConfirmationFormDto,
                                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdBookingConfirmationForm = bookingConfirmationFormService.create(bookingConfirmationFormDto);
            return ResponseEntity.ok(createdBookingConfirmationForm);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating booking confirmation form: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookingConfirmationForm(@PathVariable int id,
                                                           @Valid @RequestBody BookingConfirmationFormDto bookingConfirmationFormDto,
                                                           BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedBookingConfirmationForm = bookingConfirmationFormService.update(id, bookingConfirmationFormDto);
            return ResponseEntity.ok(updatedBookingConfirmationForm);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating booking confirmation form: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingConfirmationForm(@PathVariable int id){
        try{
            bookingConfirmationFormService.delete(id);
            return ResponseEntity.ok("Booking confirmation form deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting booking confirmation form: " + e.getMessage());
        }
    }
}