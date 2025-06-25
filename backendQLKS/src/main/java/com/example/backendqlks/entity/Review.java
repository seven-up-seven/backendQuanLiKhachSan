package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "ROOM_ID")
    private int roomId;

    @Column(name = "GUEST_ID")
    private int guestId;

    @Column(name = "RATING")
    private int rating;

    @Column(name = "COMMENT", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "REVIEWED_AT")
    private LocalDateTime reviewedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Room room;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GUEST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Guest guest;

    @PrePersist
    protected void onCreate() {
        this.reviewedAt = LocalDateTime.now();
    }
}