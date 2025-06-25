package com.example.backendqlks.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseReviewDto {
    private Integer id;
    private Integer roomId;
    private Integer guestId;
    private Integer rating;
    private String comment;
}