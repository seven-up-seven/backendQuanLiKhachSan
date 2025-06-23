package com.example.backendqlks.dto.image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotBlank(message = "Content type is required")
    private String contentType;

    @NotNull(message = "Image data is required")
    private byte[] data;

    @NotNull(message = "Room ID is required")
    private Integer roomId;
}