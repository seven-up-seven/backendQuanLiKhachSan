package com.example.backendqlks.controller;

import com.example.backendqlks.dto.review.ReviewDto;
import com.example.backendqlks.dto.review.ResponseReviewDto;
import com.example.backendqlks.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getReviewsByRoomId(@PathVariable int roomId) {
        try {
            List<ResponseReviewDto> reviews = reviewService.getReviewsByRoomId(roomId);
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Không tìm thấy đánh giá cho phòng: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách đánh giá: " + e.getMessage());
        }
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<?> getReviewsByGuestId(@PathVariable int guestId) {
        try {
            List<ResponseReviewDto> reviews = reviewService.getReviewsByGuestId(guestId);
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Không tìm thấy đánh giá cho khách: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách đánh giá: " + e.getMessage());
        }
    }

    @PostMapping("/{impactorId}/{impactor}")
    public ResponseEntity<?> createReview(@PathVariable int impactorId,
                                          @PathVariable String impactor,
                                          @RequestBody @Valid ReviewDto reviewDto,
                                          BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseReviewDto createdReview = reviewService.createReview(reviewDto, impactorId, impactor);
            return ResponseEntity.ok(createdReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Lỗi khi tạo đánh giá: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server khi tạo đánh giá: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> updateReview(@PathVariable int id,
                                          @PathVariable int impactorId,
                                          @PathVariable String impactor,
                                          @RequestBody @Valid ReviewDto reviewDto,
                                          BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }
            ResponseReviewDto updatedReview = reviewService.updateReview(id, reviewDto, impactorId, impactor);
            return ResponseEntity.ok(updatedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Lỗi khi cập nhật đánh giá: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server khi cập nhật đánh giá: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/{impactorId}/{impactor}")
    public ResponseEntity<?> deleteReview(@PathVariable int id,
                                          @PathVariable int impactorId,
                                          @PathVariable String impactor) {
        try {
            reviewService.deleteReviewById(id, impactorId, impactor);
            return ResponseEntity.ok("Xóa đánh giá thành công với ID: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Lỗi khi xóa đánh giá: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server khi xóa đánh giá: " + e.getMessage());
        }
    }
}