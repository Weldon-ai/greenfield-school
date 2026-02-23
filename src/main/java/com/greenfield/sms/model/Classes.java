package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classes")
public class Classes {

    // ================= PRIMARY KEY =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= BASIC FIELDS =================
    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_code", nullable = false, unique = true)
    private String classCode;

    @Column(name = "schedule")
    private String schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_level", nullable = false)
    private ClassLevel classLevel;

    // ================= RELATIONSHIPS =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "classes", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "class_subjects",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    // ================= CONSTRUCTORS =================
    public Classes() {}

    public Classes(String className, String classCode, String schedule, ClassLevel classLevel) {
        this.className = className;
        this.classCode = classCode;
        this.schedule = schedule;
        this.classLevel = classLevel;
    }

    // ================= GETTERS & SETTERS =================
    public Long getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public ClassLevel getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(ClassLevel classLevel) {
        this.classLevel = classLevel;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    // ================= CONVENIENCE METHODS =================
    public void addStudent(Student student) {
        if (student != null) {
            students.add(student);
            student.setClasses(this);
        }
    }

    public void removeStudent(Student student) {
        if (student != null) {
            students.remove(student);
            student.setClasses(null);
        }
    }

    public void addSubject(Subject subject) {
        if (subject != null) {
            subjects.add(subject);
            subject.getClasses().add(this);
        }
    }

    public void removeSubject(Subject subject) {
        if (subject != null) {
            subjects.remove(subject);
            subject.getClasses().remove(this);
        }
    }

    public void assignTeacher(Teacher teacher) {
        this.teacher = teacher;
        if (teacher != null && !teacher.getClasses().contains(this)) {
            teacher.getClasses().add(this);
        }
    }

    // ================= DISPLAY METHODS =================
    public String getDisplayName() {
        return className + " (" + classCode + ")";
    }

    @Override
    public String toString() {
        return "Classes{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", classCode='" + classCode + '\'' +
                '}';
    }

    // ================= THYMELEAF COMPATIBLE GETTERS =================
    public String getName() {
        return this.className;
    }

    public String getCode() {   // <-- add this!
        return this.classCode;
    }
}