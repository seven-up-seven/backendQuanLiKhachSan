package com.example.backendqlks.dto.block;

import com.example.backendqlks.entity.Floor;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class ResponseBlockDto {
    private int id;
    private String name;
    private List<Integer> floorIds;
    private List<String> floorNames;
}
