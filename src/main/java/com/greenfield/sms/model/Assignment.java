package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assignments")
public class Assignment {

    // ===== Primary Key =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // AUTO_INCREMENT in MySQL

    // ===== Basic Fields =====
    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "due_date", columnDefinition = "DATE")
    private LocalDate dueDate;

    @Column(name = "max_marks", nullable = false)
    private int maxMarks;

    // ===== ManyToOne Relationship =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false) // index and FK
    private Classes classes;

    // ===== Enum Status =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssignmentStatus status;

    @Column(name = "marks_awarded")
    private Integer marksAwarded;

    @Column(length = 1000)
    private String feedback;

    // ===== Constructors =====
    public Assignment() {}

    public Assignment(String title, String description, LocalDate dueDate, int maxMarks, Classes classes) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxMarks = maxMarks;
        this.classes = classes;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public int getMaxMarks() { return maxMarks; }
    public void setMaxMarks(int maxMarks) { this.maxMarks = maxMarks; }

    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }

    public Integer getMarksAwarded() { return marksAwarded; }
    public void setMarksAwarded(Integer marksAwarded) { this.marksAwarded = marksAwarded; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    // ===== Lifecycle Hooks =====
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = AssignmentStatus.DRAFT; // default enum value
        }
    }

    // ===== ToString =====
    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", classes=" + (classes != null ? classes.getName() : "None") +
                ", status=" + status +
                ", dueDate=" + dueDate +
                ", maxMarks=" + maxMarks +
                ", marksAwarded=" + marksAwarded +
                '}';
    }
}