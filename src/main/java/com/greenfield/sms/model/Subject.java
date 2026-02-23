package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "subjects")
public class Subject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    // ================= UPDATED RELATIONSHIPS =================

    /** * CHANGED: Removed 'mappedBy'. 
     * Subject is now the OWNER of the relationship.
     * FetchType.EAGER ensures classes load in your main table list.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "subject_classes",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<Classes> classes = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // ================= CONSTRUCTORS =================
    public Subject() {}

    public Subject(String name, String code) {
        this.name = name;
        this.code = code;
    }
// Inside Subject.java

@Column(nullable = false)
private boolean active = true; // Set default to true

// ... existing getters and setters ...

public boolean isActive() {
    return active;
}

public void setActive(boolean active) {
    this.active = active;
}
    // ================= GETTERS & SETTERS =================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Set<Classes> getClasses() { return classes; }
    public void setClasses(Set<Classes> classes) { this.classes = classes; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    // ================= CONVENIENCE METHODS =================

    public String getAssignedClassesDisplay() {
        if (classes == null || classes.isEmpty()) return "Not Assigned";
        return classes.stream()
                .map(Classes::getClassName) 
                .collect(Collectors.joining(", "));
    }

    // ================= TO STRING =================
    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", teacher=" + (teacher != null ? teacher.getFullName() : "null") +
                '}';
    }
}