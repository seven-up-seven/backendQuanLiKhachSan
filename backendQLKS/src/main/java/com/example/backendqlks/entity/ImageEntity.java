package com.example.backendqlks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "IMAGE_ENTITY")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Lob
    @Column(length = Integer.MAX_VALUE, name = "DATA")
    private byte[] data;

    @Column(name = "UPLOADED_AT")
    private LocalDateTime uploadedAt;

    //khoa ngoai toi Room
    @Column(name = "ROOM_ID")
    private int roomId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Room room;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}
