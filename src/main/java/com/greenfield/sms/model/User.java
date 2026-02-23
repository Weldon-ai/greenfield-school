package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column
    private String phone;

    @Column(nullable = false)
    private boolean enabled = true;

    // ===== SINGLE CLASS RELATIONSHIP =====
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id")
    private Classes classes; 

    // ===== ROLES =====
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ===== PRIVILEGES =====
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_privileges", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "privilege")
    private Set<String> privileges = new HashSet<>();

    // ===== ONE-TO-ONE RELATIONSHIPS =====
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Teacher teacher;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Parent parent;

    // ===== AUDIT =====
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public User() {}

    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Classes getClasses() { return classes; }
    public void setClasses(Classes classes) { this.classes = classes; }

    // Returns class name as string
    public String getClassName() {
        return (this.classes != null) ? this.classes.getName() : "Not assigned";
    }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public Set<String> getPrivileges() { return privileges; }
    public void setPrivileges(Set<String> privileges) { this.privileges = privileges; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Parent getParent() { return parent; }
    public void setParent(Parent parent) { this.parent = parent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    // ===== UTILITY METHODS =====
    public void addRole(Role role) { if (role != null) roles.add(role); }
    public void removeRole(Role role) { if (role != null) roles.remove(role); }

    public void addPrivilege(String privilege) { if (privilege != null) privileges.add(privilege); }
    public void removePrivilege(String privilege) { if (privilege != null) privileges.remove(privilege); }

    public boolean hasRole(String roleName) { return roles.stream().anyMatch(r -> r.getName().equalsIgnoreCase(roleName)); }
    public boolean hasPrivilege(String privilegeName) { return privileges.stream().anyMatch(p -> p.equalsIgnoreCase(privilegeName)); }

    public String getPrimaryRole() { return roles.stream().findFirst().map(Role::getName).orElse("STUDENT"); }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", className='" + getClassName() + '\'' +
                '}';
    }

    // ===== AUDIT CALLBACKS =====
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}