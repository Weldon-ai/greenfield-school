package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(columnDefinition = "TEXT")
    private String content; // Optional student comments

    // ✅ NEW: Field to store the name or path of the PDF file
    private String pdfPath; 

    private LocalDateTime submittedAt;

    private Integer marks;
    
    // ✅ NEW: Feedback field so the teacher can explain the grade
    @Column(columnDefinition = "TEXT")
    private String feedback;

    // ==========================
    // GETTERS & SETTERS
    // ==========================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    // Helper to set time automatically before saving
    @PrePersist
    protected void onCreate() {
        this.submittedAt = LocalDateTime.now();
    }
}