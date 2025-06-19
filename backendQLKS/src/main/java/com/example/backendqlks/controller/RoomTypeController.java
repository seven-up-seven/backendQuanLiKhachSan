package com.example.backendqlks.controller;

import com.example.backendqlks.dto.roomtype.ResponseRoomTypeDto;
import com.example.backendqlks.dto.roomtype.RoomTypeDto;
import com.example.backendqlks.service.RoomTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRoomTypes() {
        try {
            return ResponseEntity.ok(roomTypeService.getAllRoomTypes());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching room types: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomTypeById(@PathVariable int id) {
        try {
            ResponseRoomTypeDto roomType = roomTypeService.getRoomTypeById(id);
            return ResponseEntity.ok(roomType);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching room type with id: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'RECEPTIONIST')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createRoomType(@PathVariable int impactorId,
                                            @PathVariable String impactor,
                                            @RequestBody @Valid RoomTypeDto roomTypeDto,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRoomTypeDto createdRoomType = roomTypeService.createRoomType(roomTypeDto, impactorId, impactor);
            return ResponseEntity.ok(createdRoomType);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating room type: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'RECEPTIONIST')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateRoomType(@PathVariable int id,
                                            @PathVariable int impactorId,
                                            @PathVariable String impactor,
                                            @RequestBody @Valid RoomTypeDto roomTypeDto,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRoomTypeDto updatedRoomType = roomTypeService.updateRoomType(id, roomTypeDto, impactorId, impactor);
            return ResponseEntity.ok(updatedRoomType);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating room type: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'RECEPTIONIST')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteRoomType(@PathVariable int id,
                                            @PathVariable int impactorId,
                                            @PathVariable String impactor) {
        try {
            roomTypeService.deleteRoomTypeById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted room type with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting room type with id: " + e.getMessage());
        }
    }
}
