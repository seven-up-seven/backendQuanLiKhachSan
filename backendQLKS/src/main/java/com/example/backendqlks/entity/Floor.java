package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FLOOR")
@Data
public class Floor {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // TODO: check later if orphanRemoval should be false or true
    private List<Room> roomList = new ArrayList<>();

    @Column(name = "BLOCK_ID")
    private int blockId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BLOCK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Block block;
}
