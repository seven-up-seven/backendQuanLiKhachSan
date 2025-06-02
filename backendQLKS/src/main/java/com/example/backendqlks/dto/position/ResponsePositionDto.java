package com.example.backendqlks.dto.position;

import jakarta.persistence.Column;

import java.util.List;

public class ResponsePositionDto {
    private int id;
    private String name;
    private double baseSalary;
    private List<Integer> staffIds;
}
