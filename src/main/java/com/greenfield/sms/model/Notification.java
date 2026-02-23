package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;

    private boolean readStatus = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    // ================= RECIPIENT MAPPING =================
    // This handles Teachers, Admins, and Students generically
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user; 

    // Keep this for backward compatibility if your student logic needs it specifically
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = true)
    private Student student;

    private boolean adminNotification = false;

    // ===== Constructors =====
    public Notification() {}

    // Constructor for generic Users (Teachers/Admins)
    public Notification(String title, String message, User user) {
        this.title = title;
        this.message = message;
        this.user = user;
    }

    // Constructor for Students
    public Notification(String title, String message, Student student, boolean adminNotification) {
        this.title = title;
        this.message = message;
        this.student = student;
        this.adminNotification = adminNotification;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public boolean isAdminNotification() { return adminNotification; }
    public void setAdminNotification(boolean adminNotification) { this.adminNotification = adminNotification; }
    public Boolean getReadStatus() {
    return readStatus;
}
}