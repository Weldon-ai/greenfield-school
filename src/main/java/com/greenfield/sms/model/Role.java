package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    // ===== Primary Key =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== Role Name =====
    // For Spring Security, roles are usually stored as plain names (e.g., ADMIN, STUDENT)
    // We'll prepend ROLE_ when converting to GrantedAuthority in UserDetailsService
    @Column(nullable = false, unique = true)
    private String name;

    // ===== Users linked to this role =====
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    // ===== Constructors =====
    public Role() { }

    public Role(String name) {
        this.name = name;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }

    // ===== Utility Methods =====
    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
            user.getRoles().add(this); // maintain bidirectional consistency
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            this.users.remove(user);
            user.getRoles().remove(this); // maintain bidirectional consistency
        }
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usersCount=" + users.size() +
                '}';
    }
}
