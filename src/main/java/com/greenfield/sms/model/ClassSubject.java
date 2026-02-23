package com.greenfield.sms.model; // Package declaration

import jakarta.persistence.*; // JPA imports

@Entity // Marks as entity
@Table(name = "class_subjects") // Join table
public class ClassSubject {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment
    private Long id; // Mapping ID

    @ManyToOne // Many mappings to one class
    @JoinColumn(name = "class_id") // FK column
    private Classes classes; // Class entity

    @ManyToOne // Many mappings to one subject
    @JoinColumn(name = "subject_id") // FK column
    private Subject subject; // Subject entity

    @ManyToOne // Many mappings to one teacher
    @JoinColumn(name = "teacher_id") // FK column
    private Teacher teacher; // Teacher entity

    private boolean compulsory; // Compulsory or optional flag

    private boolean active = true; // Enable / disable assignment

    // Getter and setter for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public boolean isCompulsory() { return compulsory; }
    public void setCompulsory(boolean compulsory) { this.compulsory = compulsory; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
