package com.greenfield.sms.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "class_subjects",
    uniqueConstraints = @UniqueConstraint(columnNames = {"class_id", "subject_id", "teacher_id"})
)
public class ClassSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false, foreignKey = @ForeignKey(name = "fk_class_subject_class"))
    private Classes classes;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false, foreignKey = @ForeignKey(name = "fk_class_subject_subject"))
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false, foreignKey = @ForeignKey(name = "fk_class_subject_teacher"))
    private Teacher teacher;

    @Column(nullable = false)
    private boolean compulsory = false;

    @Column(nullable = false)
    private boolean active = true;

    public ClassSubject() {}

    public ClassSubject(Classes classes, Subject subject, Teacher teacher, boolean compulsory) {
        this.classes = classes;
        this.subject = subject;
        this.teacher = teacher;
        this.compulsory = compulsory;
        this.active = true;
    }

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