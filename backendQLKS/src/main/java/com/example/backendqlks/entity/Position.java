package com.example.backendqlks.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "POSITION")
public class Position {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="NAME")
    private String name;

    @Column(name="BASE_SALARY")
    private double baseSalary;
}
