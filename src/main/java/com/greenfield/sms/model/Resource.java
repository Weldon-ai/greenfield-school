package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
public class Resource {

    // ================= PRIMARY KEY =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= BASIC FIELDS =================
    @Column(nullable = false)
    private String title;       // Resource title

    @Column(length = 1000)
    private String description; // Optional description

    @Column(nullable = false)
    private String filePath;    // File path or URL

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    // ================= RELATIONSHIPS =================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;     // Teacher who uploaded the resource

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private Classes classes;     // Assigned class

    // ================= CONSTRUCTORS =================
    public Resource() {
        this.uploadedAt = LocalDateTime.now();
    }

    public Resource(String title, String description, String filePath, User uploadedBy, Classes classes) {
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.uploadedBy = uploadedBy;
        this.classes = classes;
        this.uploadedAt = LocalDateTime.now();
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }

    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    // ================= CONVENIENCE METHODS =================
    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", filePath='" + filePath + '\'' +
                ", uploadedAt=" + uploadedAt +
                ", classes=" + (classes != null ? classes.getClassName() : "null") +
                ", uploadedBy=" + (uploadedBy != null ? uploadedBy.getFullName() : "null") +
                '}';
    }
}