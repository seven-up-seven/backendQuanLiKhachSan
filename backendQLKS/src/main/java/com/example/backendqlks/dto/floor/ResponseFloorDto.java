package com.example.backendqlks.dto.floor;

import com.example.backendqlks.entity.Block;
import com.example.backendqlks.entity.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ResponseFloorDto {
    private int id;
    private String name;
    private List<Integer> roomIds;
    private Block block;
}
