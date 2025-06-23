package com.example.backendqlks.controller;

import com.example.backendqlks.dto.image.ImageDto;
import com.example.backendqlks.dto.image.ResponseImageDto;
import com.example.backendqlks.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getImagesByRoomId(@PathVariable int roomId) {
        try {
            List<ResponseImageDto> images = imageService.getImagesByRoomId(roomId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching images: " + e.getMessage());
        }
    }


    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createImage(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @RequestBody @Valid ImageDto imageDto,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseImageDto createdImage = imageService.createImage(imageDto, impactorId, impactor);
            return ResponseEntity.ok(createdImage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating image: " + e.getMessage());
        }
    }


    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateImage(@PathVariable int id,
                                         @PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @RequestBody @Valid ImageDto imageDto,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            }
            ResponseImageDto updatedImage = imageService.updateImage(id, imageDto, impactorId, impactor);
            return ResponseEntity.ok(updatedImage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating image: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteImage(@PathVariable int id,
                                         @PathVariable int impactorId,
                                         @PathVariable String impactor) {
        try {
            imageService.deleteImageById(id, impactorId, impactor);
            return ResponseEntity.ok("Deleted image with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting image: " + e.getMessage());
        }
    }
}
