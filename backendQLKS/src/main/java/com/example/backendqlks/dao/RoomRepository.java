package com.example.backendqlks.dao;

import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.enums.RoomState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Page<Room> findRoomsByRoomTypeId(int roomTypeId, Pageable pageable);

    Page<Room> findRoomsByRoomState(RoomState roomState, Pageable pageable);

    Page<Room> findByRoomStateIn(List<RoomState> roomStateList, Pageable pageable);
}
