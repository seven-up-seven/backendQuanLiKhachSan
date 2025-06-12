package com.example.backendqlks.dto.history;

import com.example.backendqlks.entity.enums.Action;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoryDto {

    private String impactor;

    private String affectedObject;

    private Integer impactorId;

    private Integer affectedObjectId;

    private Action action;

    private String content;
}
