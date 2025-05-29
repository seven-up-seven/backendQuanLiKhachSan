package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "payingGuest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoiceList = new ArrayList<>();

   @JsonIgnore
   @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<RentalFormDetail> rentalFormDetailList = new ArrayList<>();
}
