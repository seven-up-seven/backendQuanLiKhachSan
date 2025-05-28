package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "GUEST")
@Data
public class Guest {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SEX")
    private Sex sex;

    @Column(name = "AGE")
    private byte  age;

    @Column(name = "IDENTIFICATION_NUMBER")
    private String identificationNumber;

    @Column(name = "ADDRESS")
    @Nullable
    private String address;

    @Column(name = "NATIONALITY")
    private  String nationality;

    //TODO: add 2 ManyToMany relationships Guest_Invoice and Guest_BookingForm and Guest_PenaltyForm
}
