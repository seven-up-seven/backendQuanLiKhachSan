package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.RoomState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

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
    private  String name;

    @Column(name = "PRICE")
    private  double price;

    @Column(name =  "NOTE")
    private String note;

    @Column(name = "ROOM_STATE")
    private RoomState state;

    @Column(name = "ROOMTYPE_ID") // foreign key references RoomType
    private int roomTypeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOMTYPE_ID", referencedColumnName = "ID") // only for extracting related information, not create or update
    private RoomType roomType;

    //Khoa ngoai cho tang
    //Phai them vao day

    //Khoa ngoai cho phieu trich xuat
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockRequisitionInvoiceDetail> stockRequisitionInvoiceDetails;
    @Column(name ="FLOOR_ID") // foreign key references Floor
    private int  floorId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FLOOR_ID", referencedColumnName = "ID") // only for extracting related information, not create or update
    private Floor floor;

    //TODO: add OneToMay relationship Facility. A room have many facilities.
}
