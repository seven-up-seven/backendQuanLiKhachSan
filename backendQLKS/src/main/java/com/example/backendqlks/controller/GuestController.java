package com.example.backendqlks.controller;

import com.example.backendqlks.dto.guest.GuestDto;
import com.example.backendqlks.dto.guest.SearchGuestDto;
import com.example.backendqlks.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPTIONIST', 'ACCOUNTANT', 'MANAGER', 'ADMIN')")
    @GetMapping("/get-all-page")
    public ResponseEntity<?> getAllGuestsPage(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try{
            return ResponseEntity.ok(guestService.getAllPage(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guests: " + e.getMessage());
        }
    }

    @GetMapping("/account-id/{id}")
    public ResponseEntity<?> getGuestByAccountId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(guestService.getGuestByAccountId(id));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guest: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPTIONIST', 'ACCOUNTANT', 'MANAGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllGuests(){
        try {
            return ResponseEntity.ok(guestService.getAll());
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guests: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPTIONIST', 'ACCOUNTANT', 'MANAGER', 'ADMIN')")
    @GetMapping("/without-account")
    public ResponseEntity<?> getAllGuestsWithoutAccount(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        try {
            return ResponseEntity.ok(guestService.getGuestsWithoutAccount(pageable));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guests: " + e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getGuestByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(guestService.getGuestWithName(name));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guest: " + e.getMessage());
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPTIONIST', 'ACCOUNTANT', 'MANAGER', 'GUEST')")
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPTIONIST', 'ACCOUNTANT', 'MANAGER', 'GUEST')")
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

    @PostMapping("/search/{impactorId}/{impactor}")
    public ResponseEntity<?> findByMultipleCriteria(@RequestBody @Valid SearchGuestDto searchGuestDto) {
        try {
            var guest = guestService.findByMultipleCriteria(searchGuestDto.getId(), searchGuestDto.getName(), searchGuestDto.getIdentificationNumber(), searchGuestDto.getEmail(), searchGuestDto.getPhoneNumber(), searchGuestDto.getAccountId());
            return ResponseEntity.ok(guest);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error finding guest: " + e.getMessage());
        }
    }

    @GetMapping("/guest-stay")
    public ResponseEntity<?> getGuestStay() {
        try {
            return ResponseEntity.ok(guestService.getGuestStay());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching guest stay: " + e.getMessage());
        }
    }
}