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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
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

    public ResponseImageDto uploadImage(MultipartFile file, int roomId, int impactorId, String impactor) throws IOException {
        // Giả định rằng file được upload lên một nơi lưu trữ (như S3, Cloudinary) và trả về URL
        String fileUrl = uploadFileToStorage(file); // Phương thức này cần được triển khai theo hệ thống lưu trữ của bạn

        ImageEntity image = new ImageEntity();
        image.setFileName(file.getOriginalFilename());
        image.setUrl(fileUrl);
        image.setRoomId(roomId);

        ImageEntity savedImage = imageRepository.save(image);

        String content = String.format(
                "Tên file: %s; URL: %s; Mã phòng: %d",
                image.getFileName(), image.getUrl(), roomId
        );

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Ảnh")
                .affectedObjectId(savedImage.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);

        return imageMapper.toResponseDto(savedImage);
    }

    public ResponseImageDto createImage(ImageDto imageDto, int impactorId, String impactor) {
        ImageEntity image = imageMapper.toEntity(imageDto);
        ImageEntity savedImage = imageRepository.save(image);

        String content = String.format(
                "Tên file: %s; URL: %s; Mã phòng: %d",
                image.getFileName(), image.getUrl(), imageDto.getRoomId()
        );

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Ảnh")
                .affectedObjectId(savedImage.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);

        return imageMapper.toResponseDto(savedImage);
    }

    public ResponseImageDto updateImage(int id, ImageDto imageDto, int impactorId, String impactor) {
        ImageEntity image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ảnh với ID: " + id));

        StringBuilder contentBuilder = new StringBuilder();

        if (!Objects.equals(image.getFileName(), imageDto.getFileName())) {
            contentBuilder.append(String.format("Tên file: %s -> %s; ", image.getFileName(), imageDto.getFileName()));
        }
        if (!Objects.equals(image.getUrl(), imageDto.getUrl())) {
            contentBuilder.append(String.format("URL: %s -> %s; ", image.getUrl(), imageDto.getUrl()));
        }
        if (!Objects.equals(image.getRoomId(), imageDto.getRoomId())) {
            contentBuilder.append(String.format("Mã phòng: %d -> %d; ", image.getRoomId(), imageDto.getRoomId()));
        }

        image.setFileName(imageDto.getFileName());
        image.setUrl(imageDto.getUrl());
        image.setRoomId(imageDto.getRoomId());

        ImageEntity updatedImage = imageRepository.save(image);

        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Ảnh")
                    .affectedObjectId(id)
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }

        return imageMapper.toResponseDto(updatedImage);
    }

    public void deleteImageById(int id, int impactorId, String impactor) {
        ImageEntity image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ảnh với ID: " + id));

        String content = String.format(
                "Tên file: %s; URL: %s; Mã phòng: %d",
                image.getFileName(), image.getUrl(), image.getRoomId()
        );

        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Ảnh")
                .affectedObjectId(id)
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);

        imageRepository.delete(image);
    }

    @Transactional(readOnly = true)
    public ImageEntity getImageById(int id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ảnh với ID: " + id));
    }

    // Phương thức giả định để upload file và trả về URL
    private String uploadFileToStorage(MultipartFile file) throws IOException {
        // Triển khai logic upload file lên hệ thống lưu trữ (S3, Cloudinary, v.v.)
        // Trả về URL của file đã upload
        return "https://example.com/uploads/" + file.getOriginalFilename();
    }
}