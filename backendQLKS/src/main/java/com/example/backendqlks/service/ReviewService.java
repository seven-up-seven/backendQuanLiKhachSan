package com.example.backendqlks.service;

import com.example.backendqlks.dao.ReviewRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.review.ReviewDto;
import com.example.backendqlks.dto.review.ResponseReviewDto;
import com.example.backendqlks.entity.Review;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.ReviewMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final HistoryService historyService;

    public ReviewService(ReviewRepository reviewRepository,
                         ReviewMapper reviewMapper,
                         HistoryService historyService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public List<ResponseReviewDto> getReviewsByRoomId(int roomId) {
        List<Review> reviews = reviewRepository.findAllByRoom_Id(roomId);
        return reviews.stream()
                .map(reviewMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseReviewDto> getReviewsByGuestId(int guestId) {
        List<Review> reviews = reviewRepository.findAllByGuest_Id(guestId);
        return reviews.stream()
                .map(reviewMapper::toResponseDto)
                .toList();
    }

    public ResponseReviewDto createReview(ReviewDto reviewDto, int impactorId, String impactor) {
        Review review = reviewMapper.toEntity(reviewDto);
        Review savedReview = reviewRepository.save(review);

        String content = String.format(
                "Mã phòng: %d; Mã khách: %d; Điểm đánh giá: %d; Bình luận: %s",
                review.getRoomId(), review.getGuestId(), review.getRating(), review.getComment() != null ? review.getComment() : "Không có"
        );

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Đánh giá")
                .affectedObjectId(savedReview.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);

        return reviewMapper.toResponseDto(savedReview);
    }

    public ResponseReviewDto updateReview(int id, ReviewDto reviewDto, int impactorId, String impactor) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đánh giá với ID: " + id));

        StringBuilder contentBuilder = new StringBuilder();

        if (!Objects.equals(review.getRoomId(), reviewDto.getRoomId())) {
            contentBuilder.append(String.format("Mã phòng: %d -> %d; ", review.getRoomId(), reviewDto.getRoomId()));
        }
        if (!Objects.equals(review.getGuestId(), reviewDto.getGuestId())) {
            contentBuilder.append(String.format("Mã khách: %d -> %d; ", review.getGuestId(), reviewDto.getGuestId()));
        }
        if (!Objects.equals(review.getRating(), reviewDto.getRating())) {
            contentBuilder.append(String.format("Điểm đánh giá: %d -> %d; ", review.getRating(), reviewDto.getRating()));
        }
        if (!Objects.equals(review.getComment(), reviewDto.getComment())) {
            contentBuilder.append(String.format("Bình luận: %s -> %s; ",
                    review.getComment() != null ? review.getComment() : "Không có",
                    reviewDto.getComment() != null ? reviewDto.getComment() : "Không có"));
        }

        review.setRoomId(reviewDto.getRoomId());
        review.setGuestId(reviewDto.getGuestId());
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());

        Review updatedReview = reviewRepository.save(review);

        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Đánh giá")
                    .affectedObjectId(id)
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }

        return reviewMapper.toResponseDto(updatedReview);
    }

    public void deleteReviewById(int id, int impactorId, String impactor) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đánh giá với ID: " + id));

        String content = String.format(
                "Mã phòng: %d; Mã khách: %d; Điểm đánh giá: %d; Bình luận: %s",
                review.getRoomId(), review.getGuestId(), review.getRating(), review.getComment() != null ? review.getComment() : "Không có"
        );

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Đánh giá")
                .affectedObjectId(id)
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Review getReviewById(int id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đánh giá với ID: " + id));
    }
}