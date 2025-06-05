package com.example.backendqlks.dto.position;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;
@Data
public class ResponsePositionDto {
    private int id;
    private String name;
    private double baseSalary;
    private List<Integer> staffIds;
}
