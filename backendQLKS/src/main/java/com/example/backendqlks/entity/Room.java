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


    //Khoa ngoai cho tang
    //Phai them vao day

    //Khoa ngoai cho phieu trich xuat
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockRequisitionInvoiceDetail> stockRequisitionInvoiceDetails;
}
