package com.example.backendqlks.dto.image;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ResponseImageDto {
    private Integer id;
    private String fileName;
    private String contentType;
    private Integer roomId;
}
