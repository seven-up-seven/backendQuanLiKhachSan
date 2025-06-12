package com.example.backendqlks.service;

import com.example.backendqlks.dao.RoomTypeRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.roomtype.ResponseRoomTypeDto;
import com.example.backendqlks.dto.roomtype.RoomTypeDto;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.entity.enums.Action;
import com.example.backendqlks.mapper.RoomTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;
    private final HistoryService historyService;

    public RoomTypeService(RoomTypeMapper roomTypeMapper,
                           RoomTypeRepository roomTypeRepository,
                           HistoryService historyService) {
        this.roomTypeMapper = roomTypeMapper;
        this.roomTypeRepository = roomTypeRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public List<ResponseRoomTypeDto> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return roomTypeMapper.toResponseDtoList(roomTypes);
    }

    @Transactional(readOnly = true)
    public ResponseRoomTypeDto getRoomTypeById(int id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room Type with this ID cannot be found"));
        return roomTypeMapper.toResponseDto(roomType);
    }

    //check xem có room type nào có tên giống với tên trong roomTypeDto không
    public ResponseRoomTypeDto createRoomType(RoomTypeDto roomTypeDto, int impactorId, String impactor) {
        if(roomTypeRepository.findByNameContainingIgnoreCase(roomTypeDto.getName()).isPresent())
            throw new IllegalArgumentException("Room Type with this name already exists");
        RoomType roomType = roomTypeMapper.toEntity(roomTypeDto);
        roomTypeRepository.save(roomType);
        String content = String.format("Tên loại phòng: %s; Giá: %.2f", roomType.getName(), roomType.getPrice());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Loại phòng")
                .affectedObjectId(roomType.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return roomTypeMapper.toResponseDto(roomType);
    }

    public ResponseRoomTypeDto updateRoomType(int id, RoomTypeDto roomTypeDto, int impactorId, String impactor) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room Type with this ID cannot be found"));
        StringBuilder contentBuilder = new StringBuilder();
        RoomType oldRoomType = roomTypeRepository.findById(id).get();
        if (!Objects.equals(oldRoomType.getName(), roomTypeDto.getName())) {
            contentBuilder.append(String.format("Tên loại phòng: %s -> %s; ", oldRoomType.getName(), roomTypeDto.getName()));
        }
        if (!Objects.equals(oldRoomType.getPrice(), roomTypeDto.getPrice())) {
            contentBuilder.append(String.format("Giá: %.2f -> %.2f; ", oldRoomType.getPrice(), roomTypeDto.getPrice()));
        }
        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Loại phòng")
                    .affectedObjectId(roomType.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }
        roomTypeMapper.updateEntityFromDto(roomTypeDto, roomType);
        roomTypeRepository.save(roomType);
        return roomTypeMapper.toResponseDto(roomType);
    }

    public void deleteRoomTypeById(int id, int impactorId, String impactor) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room Type with this ID cannot be found"));
        String content = String.format("Tên loại phòng: %s; Giá: %.2f", roomType.getName(), roomType.getPrice());
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Loại phòng")
                .affectedObjectId(roomType.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        roomTypeRepository.delete(roomType);
    }
}
