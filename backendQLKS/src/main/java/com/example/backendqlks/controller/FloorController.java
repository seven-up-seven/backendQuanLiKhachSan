package com.example.backendqlks.controller;

import com.example.backendqlks.dto.floor.FloorDto;
import com.example.backendqlks.service.FloorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/floor")
public class FloorController {
    private final FloorService floorService;

    public FloorController(FloorService floorService){
        this.floorService = floorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFloor(@PathVariable int id){
        try{
            return ResponseEntity.ok(floorService.get(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching floor: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFloors(){
        try{
            return ResponseEntity.ok(floorService.getAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching floors: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createFloor(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @Valid @RequestBody FloorDto floorDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var createdFloor = floorService.create(floorDto, impactorId, impactor);
            return ResponseEntity.ok(createdFloor);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error creating floor: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateFloor(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @PathVariable int id,
                                         @Valid @RequestBody FloorDto floorDto,
                                         BindingResult result){
        try{
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            var updatedFloor = floorService.update(id, floorDto, impactorId, impactor);
            return ResponseEntity.ok(updatedFloor);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error updating floor: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteFloor(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @PathVariable int id){
        try{
            floorService.delete(id, impactorId, impactor);
            return ResponseEntity.ok("Floor deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting floor: " + e.getMessage());
        }
    }
}