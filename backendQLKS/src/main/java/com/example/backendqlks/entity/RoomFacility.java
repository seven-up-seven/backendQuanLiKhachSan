package com.example.backendqlks.entity;

import com.example.backendqlks.entity.compositekeys.RoomSupplyPrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ROOM_SUPPLY")
@IdClass(RoomSupplyPrimaryKey.class)
public class RoomFacility {
    @Id
    @Column(name = "ROOM_ID")
    private String roomId;

    @Id
    @Column(name = "FACILITY_ID")
    private int facilityId;

    @Column(name = "TOTAL_QUANTITY")
    private byte totalQuantity;

    @Column(name = "DAMAGED_QUANTITY")
    private byte damagedQuantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private Room room;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "ID", updatable = false, insertable = false)
    private Facility facility;
}
