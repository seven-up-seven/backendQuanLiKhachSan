package com.example.backendqlks.service;

import com.example.backendqlks.dao.FloorRepository;
import com.example.backendqlks.dao.RoomRepository;
import com.example.backendqlks.dto.history.HistoryDto;
import com.example.backendqlks.dto.room.ResponseRoomDto;
import com.example.backendqlks.dto.room.RoomDto;
import com.example.backendqlks.entity.Floor;
import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.RoomType;
import com.example.backendqlks.entity.enums.Action;
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
import java.util.Objects;

@Transactional
@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final FloorRepository floorRepository;
    private final HistoryService historyService;

    public RoomService(RoomMapper roomMapper,
                       RoomRepository roomRepository,
                       FloorRepository floorRepository,
                       HistoryService historyService) {
        this.roomMapper = roomMapper;
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.historyService = historyService;
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
    public ResponseRoomDto createRoom(RoomDto roomDto, int impactorId, String impactor) {
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
        String content = String.format(
                "Tên phòng: %s; Ghi chú: %s; Trạng thái: %s; Mã loại phòng: %d; Mã tầng: %d",
                room.getName(),
                room.getNote(),
                room.getRoomState(),
                room.getRoomTypeId(),
                room.getFloorId()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phòng")
                .affectedObjectId(room.getId())
                .action(Action.CREATE)
                .content(content)
                .build();
        historyService.create(history);
        return roomMapper.toResponseDto(room);
    }

    public ResponseRoomDto updateRoom(int id, RoomDto roomDto, int impactorId, String impactor) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room with this ID cannot be found"));
        StringBuilder contentBuilder = new StringBuilder();
        Room oldRoom = roomRepository.findById(id).get();
        if (!Objects.equals(oldRoom.getName(), roomDto.getName())) {
            contentBuilder.append(String.format("Tên phòng: %s -> %s; ", oldRoom.getName(), roomDto.getName()));
        }
        if (!Objects.equals(oldRoom.getNote(), roomDto.getNote())) {
            contentBuilder.append(String.format("Ghi chú: %s -> %s; ", oldRoom.getNote(), roomDto.getNote()));
        }
        if (!Objects.equals(oldRoom.getRoomState(), roomDto.getRoomState())) {
            contentBuilder.append(String.format("Trạng thái: %s -> %s; ", oldRoom.getRoomState(), roomDto.getRoomState()));
        }
        if (!Objects.equals(oldRoom.getRoomTypeId(), roomDto.getRoomTypeId())) {
            contentBuilder.append(String.format("Mã loại phòng: %d -> %d; ", oldRoom.getRoomTypeId(), roomDto.getRoomTypeId()));
        }
        if (!Objects.equals(oldRoom.getFloorId(), roomDto.getFloorId())) {
            contentBuilder.append(String.format("Mã tầng: %d -> %d; ", oldRoom.getFloorId(), roomDto.getFloorId()));
        }
        if (!contentBuilder.isEmpty()) {
            HistoryDto history = HistoryDto.builder()
                    .impactor(impactor)
                    .impactorId(impactorId)
                    .affectedObject("Phòng")
                    .affectedObjectId(room.getId())
                    .action(Action.UPDATE)
                    .content(contentBuilder.toString())
                    .build();
            historyService.create(history);
        }
        roomMapper.updateEntityFromDto(roomDto, room);
        roomRepository.save(room);
        return roomMapper.toResponseDto(room);
    }

    public void deleteRoomById(int id, int impactorId, String impactor) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room with this ID cannot be found"));
        String content = String.format(
                "Tên phòng: %s; Ghi chú: %s; Trạng thái: %s; Mã loại phòng: %d; Mã tầng: %d",
                room.getName(),
                room.getNote(),
                room.getRoomState(),
                room.getRoomTypeId(),
                room.getFloorId()
        );
        HistoryDto history = HistoryDto.builder()
                .impactor(impactor)
                .impactorId(impactorId)
                .affectedObject("Phòng")
                .affectedObjectId(room.getId())
                .action(Action.DELETE)
                .content(content)
                .build();
        historyService.create(history);
        roomRepository.delete(room);
    }

    @Transactional(readOnly = true)
    public List<ResponseRoomDto> findRoomsByRoomTypeId(int roomTypeId) {
        var rooms = roomRepository.findRoomsByRoomTypeId(roomTypeId);
        var availableRooms = rooms.stream()
                .filter(room -> room.getRoomState() == RoomState.READY_TO_SERVE)
                .toList();
        return roomMapper.toResponseDtoList(availableRooms);
    }
}
