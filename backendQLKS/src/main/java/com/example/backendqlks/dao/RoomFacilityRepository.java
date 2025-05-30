package com.example.backendqlks.dao;

import com.example.backendqlks.entity.RoomFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomFacilityRepository extends JpaRepository<RoomFacility, Integer> {
    List<RoomFacility> findRoomFacilitiesByRoomIdAndFacilityId(String roomId, int facilityId);
}
