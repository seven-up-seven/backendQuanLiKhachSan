package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.RoomState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROOM")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name =  "NOTE")
    private String note;

    @Column(name = "ROOM_STATE")
    @Enumerated(EnumType.STRING)
    private RoomState roomState;

    //khoa ngoai cho loai phong
    @Column(name = "ROOMTYPE_ID") // foreign key references RoomType
    private int roomTypeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOMTYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false) // only for extracting related information, not create or update
    private RoomType roomType;

    //khoa ngoai cho tang
    @Column(name ="FLOOR_ID") // foreign key references Floor
    private int  floorId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FLOOR_ID", referencedColumnName = "ID", insertable = false, updatable = false) // only for extracting related information, not create or update
    private Floor floor;

    //khoa ngoai cho booking confirmation form
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BookingConfirmationForm> bookingConfirmationForms = new ArrayList<>();

    //khoa ngoai cho rental form
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RentalForm> rentalForms = new ArrayList<>();

    //khoa ngoai toi image entity
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ImageEntity> images = new ArrayList<>();
}
