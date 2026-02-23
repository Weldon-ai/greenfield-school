package com.greenfield.sms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grade_scales")
public class GradeScale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grade;       // e.g., A, B, C
    private double minMark;     // minimum mark for grade
    private double maxMark;     // maximum mark for grade

    public GradeScale() {}

    public GradeScale(String grade, double minMark, double maxMark) {
        this.grade = grade;
        this.minMark = minMark;
        this.maxMark = maxMark;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public double getMinMark() { return minMark; }
    public void setMinMark(double minMark) { this.minMark = minMark; }

    public double getMaxMark() { return maxMark; }
    public void setMaxMark(double maxMark) { this.maxMark = maxMark; }
}
