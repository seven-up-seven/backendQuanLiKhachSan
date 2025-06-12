package com.example.backendqlks.dto.rentalform;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
@Builder
public class SearchRentalFormDto {
    private Integer roomId;
    private String roomName;
    private Integer rentalFormId;
}
