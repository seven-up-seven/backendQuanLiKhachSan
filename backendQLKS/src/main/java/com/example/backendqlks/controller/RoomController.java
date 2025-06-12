package com.example.backendqlks.controller;

import com.example.backendqlks.dto.room.ResponseRoomDto;
import com.example.backendqlks.dto.room.RoomDto;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        try {
            return ResponseEntity.ok(roomService.getAllRooms());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching rooms: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable int id) {
        try {
            ResponseRoomDto room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching room with id: " + e.getMessage());
        }
    }

    @GetMapping("/state/{roomState}")
    public ResponseEntity<?> getRoomByRoomState(@PathVariable RoomState roomState, @PageableDefault(page=0, size=10) Pageable pageable) {
        try {
            return ResponseEntity.ok(roomService.getRoomByRoomState(roomState, pageable));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching room with state: " + e.getMessage());
        }
    }

    @GetMapping("/list-state/{roomStateList}")
    public ResponseEntity<?> getRoomByListState(@PathVariable List<RoomState> roomStateList, @PageableDefault(page=0, size=10) Pageable pageable) {
        try {
            return ResponseEntity.ok(roomService.getRoomByListState(roomStateList, pageable));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching room with state: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody @Valid RoomDto roomDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRoomDto createdRoom = roomService.createRoom(roomDto);
            return ResponseEntity.ok(createdRoom);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating room: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable int id, @RequestBody @Valid RoomDto roomDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseRoomDto updatedRoom = roomService.updateRoom(id, roomDto);
            return ResponseEntity.ok(updatedRoom);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating room: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable int id) {
        try {
            roomService.deleteRoomById(id);
            return ResponseEntity.ok("Deleted room with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting room with id: " + e.getMessage());
        }
    }
}
