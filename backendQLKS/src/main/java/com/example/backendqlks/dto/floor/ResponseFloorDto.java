package com.example.backendqlks.dto.floor;

import com.example.backendqlks.entity.Block;
import com.example.backendqlks.entity.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class ResponseFloorDto {
    private int id;
    private String name;
    private List<Room> rooms;
    private Block block;
}
