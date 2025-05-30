package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.enums.RoomState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Page<Room> findRoomsByRoomTypeId(int roomTypeId, Pageable pageable);

    Page<Room> findRoomsByRoomState(RoomState roomState, Pageable pageable);
}
