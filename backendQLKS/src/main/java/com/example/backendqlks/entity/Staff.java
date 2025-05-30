package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="STAFF")
public class Staff {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="FULL_NAME")
    private String fullName;

    @Column(name="AGE")
    private int age;

    @Column(name="IDENTIFICATION_NUMBER")
    private String identificationNumber;

    @Column(name="ADDRESS")
    private String address;

    @Column(name="SEX")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name="SALARY_MULTIPLIER")
    private float salaryMultiplier;

    //khoa ngoai toi chuc vu (Position)
    @Column(name = "POSITION_ID")
    private int positionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSITION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Position position;

    //khoa ngoai toi tai khoan (Account)
    @Column(name = "ACCOUNT_ID")
    private int accountId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Account account;
}
