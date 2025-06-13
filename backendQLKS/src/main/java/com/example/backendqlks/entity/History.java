package com.example.backendqlks.entity;

import com.example.backendqlks.entity.enums.Action;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "HISTORY")
@Entity
public class History {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "IMPACTOR")
    private String impactor;

    @Column(name = "AFFECTED_OBJECT")
    private String affectedObject;

    @Column(name = "IMPACTOR_ID")
    private int impactorId;

    @Column(name = "AFFECTED_OBJECT_ID")
    private int affectedObjectId;

    @Column(name = "ACTION")
    private Action action;

    @Column(name = "EXECUTE_AT")
    private LocalDateTime executeAt;

    @Column(name = "CONTENT")
    private String content;

    @PrePersist
    protected void onCreate() {
        this.executeAt = LocalDateTime.now();
    }
}
