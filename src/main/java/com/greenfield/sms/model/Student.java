package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-One User mapping
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Admission number: must be unique and non-null
    @Column(name = "admission_number", nullable = false, unique = true)
    private String admissionNumber;

    @Column(nullable = false)
    private String email;

    private String phone;
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status = StudentStatus.ACTIVE;

    private boolean enabled = true;

    // Use LocalDate for simplicity (date only)
    @Column(name = "date_joined", nullable = false)
    private LocalDate dateJoined;

    // Current class mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_class_id")
    private Classes classes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_class_history",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<Classes> classHistory = new HashSet<>();

    // Assigned teacher mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_teacher_id")
    private Teacher assignedTeacher;

    // Subjects mapping
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_subjects",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    // ================= CONSTRUCTORS =================
    public Student() {
        this.dateJoined = LocalDate.now();
    }

    // ================= HELPER METHODS =================
    public void setClasses(Classes classes) {
        this.classes = classes;
        if (classes != null) this.classHistory.add(classes);
    }

    public void enrollInSubject(Subject subject) {
        if (subject != null) this.subjects.add(subject);
    }

    public void promoteTo(Classes nextClass) {
        setClasses(nextClass);
    }

    public String getFullName() {
        return (user != null && user.getFullName() != null) ? user.getFullName() : "New Student";
    }

    public Classes getCurrentClass() {
        return this.classes;
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAdmissionNumber() { return admissionNumber; }
    public void setAdmissionNumber(String admissionNumber) {
        if (admissionNumber == null || admissionNumber.isBlank()) {
            this.admissionNumber = "ADM-" + System.currentTimeMillis();
        } else {
            this.admissionNumber = admissionNumber;
        }
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDate getDateJoined() { return dateJoined; }
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }

    public Classes getClasses() { return classes; }

    public Set<Classes> getClassHistory() { return classHistory; }

    public Teacher getAssignedTeacher() { return assignedTeacher; }
    public void setAssignedTeacher(Teacher assignedTeacher) { this.assignedTeacher = assignedTeacher; }

    public Set<Subject> getSubjects() { return subjects; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    // ================= JPA CALLBACKS =================
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dateJoined == null) dateJoined = LocalDate.now();
        if (admissionNumber == null || admissionNumber.isBlank()) {
            admissionNumber = "ADM-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Student[id=" + id + ", admissionNumber=" + admissionNumber + "]";
    }
}
