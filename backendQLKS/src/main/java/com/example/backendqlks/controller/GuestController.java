package com.example.backendqlks.controller;

import com.example.backendqlks.dto.guest.GuestDto;
import com.example.backendqlks.dto.guest.SearchGuestDto;
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

    @GetMapping("/get-all-page")
    public ResponseEntity<?> getAllGuestsPage(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try{
            return ResponseEntity.ok(guestService.getAllPage(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guests: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGuests(){
        try {
            return ResponseEntity.ok(guestService.getAll());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guests: " + e.getMessage());
        }
    }

    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createGuest(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @Valid @RequestBody GuestDto guestDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdGuest = guestService.create(guestDto, impactorId, impactor);
            return ResponseEntity.ok(createdGuest);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating guest: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateGuest(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @PathVariable int id,
                                         @Valid @RequestBody GuestDto guestDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedGuest = guestService.update(id, guestDto, impactorId, impactor);
            return ResponseEntity.ok(updatedGuest);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating guest: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteGuest(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @PathVariable int id){
        try{
            guestService.delete(id, impactorId, impactor);
            return ResponseEntity.ok("Guest deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting guest: " + e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> findByMultipleCriteria(@RequestBody @Valid SearchGuestDto searchGuestDto) {
        try {
            var guest = guestService.findByMultipleCriteria(searchGuestDto.getId(), searchGuestDto.getName(), searchGuestDto.getIdentificationNumber(), searchGuestDto.getEmail(), searchGuestDto.getPhoneNumber(), searchGuestDto.getAccountId());
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error finding guest: " + e.getMessage());
        }
    }
}