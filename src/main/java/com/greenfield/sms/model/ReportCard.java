package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "report_cards")
public class ReportCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Classes classes;

    private String term;
    private Double totalMarks;
    private String meanGrade;
    private String automatedRemark;

    public ReportCard() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public Double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Double totalMarks) { this.totalMarks = totalMarks; }

    public String getMeanGrade() { return meanGrade; }
    public void setMeanGrade(String meanGrade) { this.meanGrade = meanGrade; }

    public String getAutomatedRemark() { return automatedRemark; }
    public void setAutomatedRemark(String automatedRemark) { this.automatedRemark = automatedRemark; }
}