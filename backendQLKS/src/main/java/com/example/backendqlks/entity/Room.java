package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.RoomState;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ROOM")
@Data
public class Room {
    @Id
    @Column(name = "ID")
    private String id; // lay ma phong lam id luon vd P1.16

    @Column(name = "NAME")
    private  String name;

    @Column(name = "PRICE")
    private  double price;

    @Column(name =  "NOTE")
    private String note;

    @Column(name = "ROOM_STATE")
    private RoomState state;

    @Column(name = "ROOMTYPE_ID")
    private int roomTypeId;


    //TangId int [ref: > Tang.Id]
}
