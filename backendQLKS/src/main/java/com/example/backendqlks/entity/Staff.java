package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.EnableMBeanExport;

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

    @Column(name="CCCD")
    private String cccd;

    @Column(name="ADDRESS")
    private String address;

    @Column(name="GENDER")
    private boolean gender;

    @Column(name="SALARY_MULTIPLIER")
    private double salaryMultiplier;

    //khoa ngoai toi chuc vu (UserType)
    @Column(name = "USER_TYPE_ID")
    private int userTypeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_TYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UserType userType;

    //khoa ngoai toi tai khoan (Account)
    @Column(name = "ACCOUNT_ID")
    private int accountId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Account account;
}
