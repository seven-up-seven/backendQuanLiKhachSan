package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BLOCK")
@Data
public class Block {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name =  "NAME")
    private String name;

    @Column(name = "POS_X")
    private Double posX;

    @Column(name = "POS_Y")
    private Double posY;

    @JsonIgnore
    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Floor> floors = new ArrayList<>();
}
