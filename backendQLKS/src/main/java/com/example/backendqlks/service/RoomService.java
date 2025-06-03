package com.example.backendqlks.service;

import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.room.ResponseRoomDto;
import com.example.backendqlks.dto.room.RoomDto;
import com.example.backendqlks.entity.Floor;
import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.entity.enums.RoomState;
import com.example.backendqlks.mapper.RoomMapper;
import org.springframework.data.domain.Page;
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

    public RoomService(RoomMapper roomMapper, RoomRepository roomRepository) {
        this.roomMapper = roomMapper;
        this.roomRepository = roomRepository;
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
    public List<ResponseRoomDto> getRoomByRoomState(RoomState roomState, Pageable pageable) {
        Page<Room> roomPage=roomRepository.findRoomsByRoomState(roomState, pageable);
        return roomMapper.toResponseDtoList(roomPage.getContent());
    }

    public ResponseRoomDto createRoom(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);
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
        //delete related foreign fields
        RoomType roomType = room.getRoomType();
        roomType.getRooms().remove(room);
        Floor floor=room.getFloor();
        floor.getRooms().remove(room);
        roomRepository.delete(room);
    }
}
