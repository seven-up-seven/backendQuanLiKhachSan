package com.example.backendqlks.service;

import com.example.backendqlks.dao.FloorRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.room.ResponseRoomDto;
import com.example.backendqlks.dto.room.RoomDto;
import com.example.backendqlks.entity.Floor;
import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.RoomMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Transactional
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final FloorRepository floorRepository;

    public RoomService(RoomMapper roomMapper, RoomRepository roomRepository, FloorRepository floorRepository) {
        this.roomMapper = roomMapper;
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseRoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.toResponseDtoList(rooms);
    }

    @Transactional(readOnly = true)
    public ResponseRoomDto getRoomById(int id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room with this ID cannot be found"));
        return roomMapper.toResponseDto(room);
    }

    @Transactional(readOnly = true)
    public Page<ResponseRoomDto> getRoomByRoomState(RoomState roomState, Pageable pageable) {
        Page<Room> roomPage=roomRepository.findRoomsByRoomState(roomState, pageable);
        List<ResponseRoomDto> dtoList = roomMapper.toResponseDtoList(roomPage.getContent());
        return new PageImpl<>(dtoList, pageable, roomPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ResponseRoomDto> getRoomByListState(List<RoomState> roomStateList, Pageable pageable) {
        Page<Room> roomPage=roomRepository.findByRoomStateIn(roomStateList, pageable);
        List<ResponseRoomDto> dtoList = roomMapper.toResponseDtoList(roomPage.getContent());
        return new PageImpl<>(dtoList, pageable, roomPage.getTotalElements());
    }

    //check hợp lệ, sau đó check xem trùng tên k mới cho tạo mới
    public ResponseRoomDto createRoom(RoomDto roomDto) {
        var floorId = roomDto.getFloorId();
        if(floorId == null) {
            throw new IllegalArgumentException("Floor ID cannot be null");
        }
        var floor = floorRepository.findById(floorId);
        if(floor.isEmpty()) {
            throw new IllegalArgumentException("Floor with this ID cannot be found");
        }
        Room room = roomMapper.toEntity(roomDto);
        if(floor.get().getRooms().stream().anyMatch(r -> r.getName().equalsIgnoreCase(room.getName()))) {
            throw new IllegalArgumentException("Room already exists on this floor");
        }
        roomRepository.save(room);
        return roomMapper.toResponseDto(room);
    }

    public ResponseRoomDto updateRoom(int id, RoomDto roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room with this ID cannot be found"));
        roomMapper.updateEntityFromDto(roomDto, room);
        roomRepository.save(room);
        return roomMapper.toResponseDto(room);
    }

    public void deleteRoomById(int id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room with this ID cannot be found"));
        roomRepository.delete(room);
    }
}
