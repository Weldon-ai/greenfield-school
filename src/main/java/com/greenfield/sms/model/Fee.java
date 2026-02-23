package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fees")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description; // e.g., Tuition, Exam Fee, Library Fee
    private double amount;      // Total amount
    private double paidAmount;  // Amount already paid
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    public Fee() {}

    public Fee(String description, double amount, double paidAmount, LocalDate dueDate, Student student) {
        this.description = description;
        this.amount = amount;
        this.paidAmount = paidAmount;
        this.dueDate = dueDate;
        this.student = student;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public double getPendingAmount() { return amount - paidAmount; }
}
