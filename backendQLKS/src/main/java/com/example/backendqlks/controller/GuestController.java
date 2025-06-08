package com.example.backendqlks.controller;

import com.example.backendqlks.dto.guest.GuestDto;
import com.example.backendqlks.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/guest")
public class GuestController {
    private final GuestService guestService;

    public GuestController(GuestService guestService){
        this.guestService = guestService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGuest(@PathVariable int id){
        try{
            return ResponseEntity.ok(guestService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guest: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGuests(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try{
            return ResponseEntity.ok(guestService.getAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guests: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createGuest(@Valid @RequestBody GuestDto guestDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdGuest = guestService.create(guestDto);
            return ResponseEntity.ok(createdGuest);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating guest: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuest(@PathVariable int id,
                                         @Valid @RequestBody GuestDto guestDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedGuest = guestService.update(id, guestDto);
            return ResponseEntity.ok(updatedGuest);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating guest: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGuest(@PathVariable int id){
        try{
            guestService.delete(id);
            return ResponseEntity.ok("Guest deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting guest: " + e.getMessage());
        }
    }
}