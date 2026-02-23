package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parents")
public class Parent {

    // ===== Primary Key =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Link to User =====
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // ===== Link to Students =====
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "parent_students",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    // ===== Contact Info =====
    @Column
    private String phone;

    @Column
    private String address;

    // ===== Audit Fields =====
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    // ===== Constructors =====
    public Parent() {}

    public Parent(User user) {
        this.user = user;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Student> getStudents() { return students; }
    public void setStudents(Set<Student> students) { this.students = students; }

    public void addStudent(Student student) { if (student != null) this.students.add(student); }
    public void removeStudent(Student student) { if (student != null) this.students.remove(student); }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    // ===== Lifecycle Hooks =====
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===== Convenience =====
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", students=" + students.size() +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
