package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROOMTYPE")
@Data
public class RoomType {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private  String name;

    @Column(name = "PRICE")
    private  Double price;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true) // TODO: check later if orphanRemoval should be false or true
    private List<Room> roomList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true) // TODO: check later if orphanRemoval should be false or true
    private List<RevenueReportDetail> revenueReportDetailList = new ArrayList<>();
}
