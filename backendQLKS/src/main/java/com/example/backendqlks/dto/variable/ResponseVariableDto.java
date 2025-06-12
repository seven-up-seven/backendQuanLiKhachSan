package com.example.backendqlks.dto.variable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseVariableDto {
    private int id;
    private String name;
    private double value;
    private String description;
}
