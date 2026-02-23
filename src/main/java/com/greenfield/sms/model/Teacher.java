package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Username defaults to email, unique
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    private String gender;

    @Column(nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeacherStatus status = TeacherStatus.ACTIVE;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private LocalDate dateJoined = LocalDate.now();

    // ===== Link to User (admin-managed credentials) =====
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    // Classes taught by teacher
    @ManyToMany
    @JoinTable(
        name = "teacher_classes",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private List<Classes> classes = new ArrayList<>();

    // ===== Constructors =====
    public Teacher() {} // No-arg constructor required by JPA

    public Teacher(String firstName, String lastName, String email, String subject, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = email; // default username
        this.subject = subject;
        this.user = user;
        this.status = TeacherStatus.ACTIVE;
        this.enabled = true;
        this.dateJoined = LocalDate.now();
    }
// ===== Teacher Password =====
@Column(nullable = false)
private String password;

public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
        if (this.username == null) this.username = email; // sync username
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public TeacherStatus getStatus() { return status; }
    public void setStatus(TeacherStatus status) { this.status = status; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public LocalDate getDateJoined() { return dateJoined; }
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Classes> getClasses() { return classes; }
    public void setClasses(List<Classes> classes) { this.classes = classes; }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", fullName='" + getFullName() + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", status=" + status +
                '}';
    }
}
