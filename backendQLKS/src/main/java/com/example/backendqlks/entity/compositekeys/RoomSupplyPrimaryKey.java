package com.example.backendqlks.entity.compositekeys;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class RoomSupplyPrimaryKey implements Serializable {
    private String roomId;
    private int permissionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof  RoomSupplyPrimaryKey other) {
            return (this.roomId!=null && this.roomId.equals(other.roomId)) && (this.permissionId == other.permissionId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, permissionId);
    }
}
