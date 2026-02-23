package com.greenfield.sms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int marks;

    private String grade;
    private String remarks;

    // ================= RELATIONSHIPS =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // ================= CONSTRUCTORS =================
    public Result() {}

    public Result(Student student, Exam exam, Subject subject, int marks) {
        this.student = student;
        this.exam = exam;
        this.subject = subject;
        this.marks = marks;
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    // Compatibility for old code expecting getMarksObtained()
    public int getMarksObtained() { return marks; }
    public void setMarksObtained(int marks) { this.marks = marks; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    // ================= DISPLAY / TO_STRING =================
    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", marks=" + marks +
                ", grade='" + grade + '\'' +
                ", student=" + (student != null ? student.getFullName() : "null") +
                ", exam=" + (exam != null ? exam.getName() : "null") +  // ✅ use getName()
                ", subject=" + (subject != null ? subject.getName() : "null") +
                '}';
    }
}