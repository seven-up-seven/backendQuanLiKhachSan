package com.example.backendqlks.service;

import com.example.backendqlks.dao.ImageRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.image.ImageDto;
import com.example.backendqlks.dto.image.ResponseImageDto;
import com.example.backendqlks.entity.ImageEntity;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.ImageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final HistoryService historyService;

    public ImageService(ImageRepository imageRepository,
                        ImageMapper imageMapper,
                        HistoryService historyService) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public List<ResponseImageDto> getImagesByRoomId(int roomId) {
        List<ImageEntity> images = imageRepository.findAllByRoom_Id(roomId);
        return images.stream()
                .map(imageMapper::toResponseDto)
                .toList();
    }

    public ResponseImageDto createImage(ImageDto imageDto, int impactorId, String impactor) {
        ImageEntity image = imageMapper.toEntity(imageDto);
        imageRepository.save(image);

        String content = String.format(
                "Tên file: %s; Loại file: %s; Mã phòng: %d",
                image.getFileName(),
                image.getContentType(),
                image.getRoom().getId()
        );

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Ảnh")
                .affectedObjectId(image.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return imageMapper.toResponseDto(image);
    }

    public ResponseImageDto updateImage(int id, ImageDto imageDto, int impactorId, String impactor) {
        ImageEntity image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image with this ID cannot be found"));

        StringBuilder contentBuilder = new StringBuilder();

        if (!Objects.equals(image.getFileName(), imageDto.getFileName())) {
            contentBuilder.append(String.format("Tên file: %s -> %s; ", image.getFileName(), imageDto.getFileName()));
        }
        if (!Objects.equals(image.getContentType(), imageDto.getContentType())) {
            contentBuilder.append(String.format("Loại file: %s -> %s; ", image.getContentType(), imageDto.getContentType()));
        }
        if (!Objects.equals(image.getRoom().getId(), imageDto.getRoomId())) {
            contentBuilder.append(String.format("Mã phòng: %d -> %d; ", image.getRoom().getId(), imageDto.getRoomId()));
        }

        image.setFileName(imageDto.getFileName());
        image.setContentType(imageDto.getContentType());
        image.setData(imageDto.getData());
        image.setRoom(imageMapper.mapRoomIdToRoom(imageDto.getRoomId()));
        imageRepository.save(image);

        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Ảnh")
                    .affectedObjectId(image.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }

        return imageMapper.toResponseDto(image);
    }

    public void deleteImageById(int id, int impactorId, String impactor) {
        ImageEntity image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image with this ID cannot be found"));

        String content = String.format(
                "Tên file: %s; Loại file: %s; Mã phòng: %d",
                image.getFileName(),
                image.getContentType(),
                image.getRoom().getId()
        );

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Ảnh")
                .affectedObjectId(image.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);

        imageRepository.delete(image);
    }
}
