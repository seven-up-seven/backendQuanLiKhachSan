package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="ACCOUNT")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(name="USERNAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    //khoa ngoai toi vai tro cua tai khoan
    @Column(name = "USER_ROLE_ID")
    private int userRoleId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ROLE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private UserRole userRole;

    //khoa ngoai toi Guest
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Guest guest;

    //khoa ngoai toi Staff
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Staff staff;
}
