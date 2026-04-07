package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(
    name = "attendance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "class_id", "date"})
    },
    indexes = {
        @Index(name = "idx_student_id", columnList = "student_id"),
        @Index(name = "idx_class_id", columnList = "class_id")
    }
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= RELATIONSHIPS =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Classes classes;

    // ================= ATTENDANCE DATA =================
    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean present;

    @Enumerated(EnumType.STRING)
    @Column(name = "recorded_by", nullable = false, length = 20)
    private RecordedBy recordedBy;

    @Column(name = "recorded_by_user_id")
    private Long recordedByUserId;

    // ================= LOCKING SUPPORT =================
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean locked = false;

    @Column(name = "locked_at", columnDefinition = "DATETIME")
    private LocalDateTime lockedAt;

    // ================= AUDIT FIELDS =================
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    // ================= CONSTRUCTORS =================
    public Attendance() {}

    public Attendance(Student student, Classes classes,
                      LocalDate date, boolean present,
                      RecordedBy recordedBy) {
        this.student = student;
        this.classes = classes;
        this.date = date;
        this.present = present;
        this.recordedBy = recordedBy;
    }

    // ================= LIFECYCLE =================
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ================= BUSINESS LOGIC =================
    @Transient
    public boolean isEditable(int lockAfterDays) {
        if (locked) return false;
        long daysPassed = ChronoUnit.DAYS.between(date, LocalDate.now());
        return daysPassed <= lockAfterDays;
    }

    public void lock() {
        this.locked = true;
        this.lockedAt = LocalDateTime.now();
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public RecordedBy getRecordedBy() { return recordedBy; }
    public void setRecordedBy(RecordedBy recordedBy) { this.recordedBy = recordedBy; }

    public Long getRecordedByUserId() { return recordedByUserId; }
    public void setRecordedByUserId(Long recordedByUserId) { this.recordedByUserId = recordedByUserId; }

    public boolean isLocked() { return locked; }
    public LocalDateTime getLockedAt() { return lockedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ================= CONVENIENCE =================
    @Transient
    public Teacher getTeacher() {
        return classes != null ? classes.getTeacher() : null;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", student=" + (student != null ? student.getFullName() : "None") +
                ", classes=" + (classes != null ? classes.getClassName() : "None") +
                ", date=" + date +
                ", present=" + present +
                ", locked=" + locked +
                '}';
    }
}