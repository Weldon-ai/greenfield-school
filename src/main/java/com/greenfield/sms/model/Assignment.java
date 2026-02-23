package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDate dueDate;

    private int maxMarks;

    // ================= UPDATED RELATION =================
    /**
     * Relationship updated to reference the 'Classes' entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id") 
    private Classes classes; // Renamed type from SchoolClass to Classes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status = AssignmentStatus.DRAFT;

    private Integer marksAwarded;

    @Column(length = 1000)
    private String feedback;

    // ================= CONSTRUCTORS =================
    public Assignment() {}

    public Assignment(String title, String description, LocalDate dueDate, int maxMarks, Classes classes) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxMarks = maxMarks;
        this.classes = classes;
        this.status = AssignmentStatus.DRAFT;
    }

    // ================= GETTERS & SETTERS =================
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

    /**
     * Getter returns 'Classes' type to match the new model name
     */
    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }

    public Integer getMarksAwarded() { return marksAwarded; }
    public void setMarksAwarded(Integer marksAwarded) { this.marksAwarded = marksAwarded; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    // ================= TO STRING =================
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