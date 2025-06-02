package com.example.backendqlks.dto.block;

import com.example.backendqlks.entity.Floor;
import jakarta.persistence.Column;

import java.util.List;

public class ResponseBlockDto {
    private int id;
    private String name;
    private List<Integer> floorIds;
}
