package com.example.backendqlks.service;

import com.example.backendqlks.dao.RoomTypeRepository;
import com.example.backendqlks.dto.roomtype.ResponseRoomTypeDto;
import com.example.backendqlks.dto.roomtype.RoomTypeDto;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.mapper.RoomTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeService(RoomTypeMapper roomTypeMapper, RoomTypeRepository roomTypeRepository) {
        this.roomTypeMapper = roomTypeMapper;
        this.roomTypeRepository = roomTypeRepository;
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

    public ResponseRoomTypeDto createRoomType(RoomTypeDto roomTypeDto) {
        RoomType roomType = roomTypeMapper.toEntity(roomTypeDto);
        roomTypeRepository.save(roomType);
        return roomTypeMapper.toResponseDto(roomType);
    }

    public ResponseRoomTypeDto updateRoomType(int id, RoomTypeDto roomTypeDto) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room Type with this ID cannot be found"));
        roomTypeMapper.updateEntityFromDto(roomTypeDto, roomType);
        roomTypeRepository.save(roomType);
        return roomTypeMapper.toResponseDto(roomType);
    }

    public void deleteRoomTypeById(int id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room Type with this ID cannot be found"));
        //considering checking foreign key relation before deleting
        //roomTypeRepository.delete(roomType);
    }
}
