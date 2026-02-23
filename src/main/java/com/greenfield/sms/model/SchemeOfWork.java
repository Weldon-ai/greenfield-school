package com.greenfield.sms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schemes_of_work")
@Data
public class SchemeOfWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For professional document tracking (e.g., SCH-2026-X123)
    private String referenceNumber;

    @Column(nullable = false)
    private String term;

    @Column(nullable = false)
    private Integer weekNumber;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String targetClass;

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT")
    private String objectives;

    @Column(columnDefinition = "TEXT")
    private String resources;

    private String teacherName; // Who filled this out
    private String status = "PENDING"; 
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        // Generate a simple reference number automatically
        this.referenceNumber = "SCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}