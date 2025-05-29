package com.example.backendqlks.entity;

import com.example.backendqlks.entity.compositekeys.RoomSupplyPrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ROOM_SUPPLY")
@IdClass(RoomSupplyPrimaryKey.class)
public class RoomsSupply {
    @Id
    @Column(name = "ROOM_ID")
    private String roomId;

    @Id
    @Column(name = "SUPPLY_ID")
    private int supplyId;

    @Column(name = "TOTAL_NUMBER")
    private int totalNumber;

    @Column(name = "DAMAGED_NUMBER")
    private int damagedNumber;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private Room room;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPPLY_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private Supply supply;
}
