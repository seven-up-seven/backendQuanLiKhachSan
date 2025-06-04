package com.example.backendqlks.controller;

import com.example.backendqlks.dto.roomtype.ResponseRoomTypeDto;
import com.example.backendqlks.dto.roomtype.RoomTypeDto;
import com.example.backendqlks.service.RoomTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> createRoomType(@RequestBody @Valid RoomTypeDto roomTypeDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRoomTypeDto createdRoomType = roomTypeService.createRoomType(roomTypeDto);
            return ResponseEntity.ok(createdRoomType);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating room type: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoomType(@PathVariable int id, @RequestBody @Valid RoomTypeDto roomTypeDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRoomTypeDto updatedRoomType = roomTypeService.updateRoomType(id, roomTypeDto);
            return ResponseEntity.ok(updatedRoomType);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating room type: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomType(@PathVariable int id) {
        try {
            roomTypeService.deleteRoomTypeById(id);
            return ResponseEntity.ok("Deleted room type with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting room type with id: " + e.getMessage());
        }
    }
}
