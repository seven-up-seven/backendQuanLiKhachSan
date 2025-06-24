package com.example.backendqlks.controller;

import com.example.backendqlks.dto.image.ImageDto;
import com.example.backendqlks.dto.image.ResponseImageDto;
import com.example.backendqlks.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Không tìm thấy ảnh cho phòng: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách ảnh: " + e.getMessage());
        }
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("roomId") int roomId,
                                         @RequestParam("impactorId") int impactorId,
                                         @RequestParam("impactor") String impactor) {
        try {
            ResponseImageDto image = imageService.uploadImage(file, roomId, impactorId, impactor);
            return ResponseEntity.ok(image);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Lỗi khi upload ảnh: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server khi upload ảnh: " + e.getMessage());
        }
    }

    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createImage(@PathVariable int impactorId,
                                         @PathVariable String impactor,
                                         @RequestBody @Valid ImageDto imageDto,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseImageDto createdImage = imageService.createImage(imageDto, impactorId, impactor);
            return ResponseEntity.ok(createdImage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Lỗi khi tạo ảnh: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server khi tạo ảnh: " + e.getMessage());
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
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseImageDto updatedImage = imageService.updateImage(id, imageDto, impactorId, impactor);
            return ResponseEntity.ok(updatedImage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Lỗi khi cập nhật ảnh: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server khi cập nhật ảnh: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteImage(@PathVariable int id,
                                         @PathVariable int impactorId,
                                         @PathVariable String impactor) {
        try {
            imageService.deleteImageById(id, impactorId, impactor);
            return ResponseEntity.ok("Xóa ảnh thành công với ID: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Lỗi khi xóa ảnh: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server khi xóa ảnh: " + e.getMessage());
        }
    }
}