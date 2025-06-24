package com.example.backendqlks.mapper;

import com.example.backendqlks.dto.review.ReviewDto;
import com.example.backendqlks.dto.review.ResponseReviewDto;
import com.example.backendqlks.entity.ImageEntity;
import com.example.backendqlks.entity.Review;
import com.example.backendqlks.entity.Room;
import com.example.backendqlks.entity.Guest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "room", source = "roomId", qualifiedByName = "mapRoomIdToRoom")
    @Mapping(target = "guest", source = "guestId", qualifiedByName = "mapGuestIdToGuest")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    Review toEntity(ReviewDto dto);

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "guestId", source = "guest.id")
    ReviewDto toDto(Review entity);

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "guestId", source = "guest.id")
    ResponseReviewDto toResponseDto(Review entity);

    @Named("mapRoomIdToRoom")
    default Room mapRoomIdToRoom(Integer roomId) {
        if (roomId == null) return null;
        Room room = new Room();
        room.setId(roomId);
        return room;
    }

    @Named("mapGuestIdToGuest")
    default Guest mapGuestIdToGuest(Integer guestId) {
        if (guestId == null) return null;
        Guest guest = new Guest();
        guest.setId(guestId);
        return guest;
    }
}