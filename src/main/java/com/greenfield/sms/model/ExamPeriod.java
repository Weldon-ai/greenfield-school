package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exam_periods")
public class ExamPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., Term 1, Term 2

    private LocalDate startDate;
    private LocalDate endDate;

    public ExamPeriod() {}

    public ExamPeriod(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
