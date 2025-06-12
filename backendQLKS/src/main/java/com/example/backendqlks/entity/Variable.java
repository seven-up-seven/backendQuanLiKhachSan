package com.example.backendqlks.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "VARIABLE")
@Data
public class Variable {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "VALUE")
    private double value;
    @Column(name = "DESCRIPTION")
    private String description;
}
