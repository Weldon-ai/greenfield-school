package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;         // Exam name (for Result display)
    private String subject;
    private String term;
    private LocalDate examDate;
    private String examType;
    private Integer maxMarks = 100;

    @Enumerated(EnumType.STRING)
    private ExamStatus status = ExamStatus.DRAFT;

    // ================= CLASSES & TEACHER =================
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classes classes;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "exam_period_id")
    private ExamPeriod examPeriod;

    private boolean locked = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exam_id")
    private List<ExamResult> results = new ArrayList<>();

    @Transient
    private static List<GradeScale> gradeScales = new ArrayList<>();

    // ================= CONSTRUCTORS =================
    public Exam() {}

    public Exam(String name, String subject, String term, LocalDate examDate, Classes classes) {
        this.name = name;
        this.subject = subject;
        this.term = term;
        this.examDate = examDate;
        this.classes = classes;
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }

    public String getName() { return name; }  // ✅ Needed for Result display
    public void setName(String name) { this.name = name; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }

    public ExamStatus getStatus() { return status; }
    public void setStatus(ExamStatus status) { this.status = status; }

    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public ExamPeriod getExamPeriod() { return examPeriod; }
    public void setExamPeriod(ExamPeriod examPeriod) { this.examPeriod = examPeriod; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public List<ExamResult> getResults() { return results; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ================= GRADE SCALE =================
    public static void setGradeScales(List<GradeScale> scales) { gradeScales = scales; }

    public String calculateGrade(Double marks) {
        for (GradeScale scale : gradeScales) {
            if (marks >= scale.getMinMark() && marks <= scale.getMaxMark()) {
                return scale.getGrade();
            }
        }
        return "N/A";
    }

    // ================= EXAM RESULT MANAGEMENT =================
    public void addOrUpdateResult(ExamResult result) {
        if (locked) return;
        removeResult(result);
        if (result.getMarks() != null) {
            result.setGrade(calculateGrade(result.getMarks()));
        }
        results.add(result);
        updatedAt = LocalDateTime.now();
    }

    public void removeResult(ExamResult result) {
        results.removeIf(r -> r.getStudent().getId().equals(result.getStudent().getId()));
        updatedAt = LocalDateTime.now();
    }

    // ================= ANALYTICS =================
    public double getAverageMark() {
        return results.stream()
                .filter(r -> r.getMarks() != null)
                .mapToDouble(ExamResult::getMarks)
                .average()
                .orElse(0.0);
    }

    public double getHighestMark() {
        return results.stream()
                .filter(r -> r.getMarks() != null)
                .mapToDouble(ExamResult::getMarks)
                .max()
                .orElse(0.0);
    }

    public double getLowestMark() {
        return results.stream()
                .filter(r -> r.getMarks() != null)
                .mapToDouble(ExamResult::getMarks)
                .min()
                .orElse(0.0);
    }

    public long getPassCount() {
        return results.stream()
                .filter(r -> r.getMarks() != null && r.getMarks() >= 50)
                .count();
    }

    public long getFailCount() {
        return results.stream()
                .filter(r -> r.getMarks() != null && r.getMarks() < 50)
                .count();
    }

    public List<ExamResult> getRankedResults() {
        return results.stream()
                .sorted(Comparator.comparing(ExamResult::getMarks).reversed())
                .collect(Collectors.toList());
    }

    // ================= ENUMS =================
    public enum ExamStatus {
        DRAFT, OPEN, CLOSED, PUBLISHED
    }

    // ================= NESTED CLASS =================
    @Entity
    @Table(name = "exam_results")
    public static class ExamResult {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "student_id")
        private Student student;

        private Double marks;
        private String grade;
        private String remarks;

        public ExamResult() {}

        public ExamResult(Student student, Double marks, String grade, String remarks) {
            this.student = student;
            this.marks = marks;
            this.grade = grade;
            this.remarks = remarks;
        }

        public Long getId() { return id; }

        public Student getStudent() { return student; }
        public void setStudent(Student student) { this.student = student; }

        public Double getMarks() { return marks; }
        public void setMarks(Double marks) { this.marks = marks; }

        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }

        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }
}