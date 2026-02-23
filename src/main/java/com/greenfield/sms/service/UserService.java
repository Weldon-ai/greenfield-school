package com.greenfield.sms.service;

import com.greenfield.sms.model.*;
import com.greenfield.sms.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       ParentRepository parentRepository,
                       AdminRepository adminRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.parentRepository = parentRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =================== CURRENT USER HELPERS ===================
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) return null;
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }

    public User getCurrentAdmin() {
        User current = getCurrentUser();
        return (current != null && userHasRole(current, "ADMIN")) ? current : null;
    }

    public User getCurrentTeacher() {
        User current = getCurrentUser();
        return (current != null && userHasRole(current, "TEACHER")) ? current : null;
    }

    // =================== FIND USERS ===================
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findAll() {
        return getAllUsers();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // =================== ROLE HELPERS ===================
    public boolean userHasRole(User user, String roleName) {
        if (user == null || user.getRoles() == null) return false;
        return user.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase(roleName));
    }

    public boolean currentUserHasRole(String roleName) {
        User current = getCurrentUser();
        return current != null && userHasRole(current, roleName);
    }

    // 🔥 UPDATED: Now also ensures domain entity exists
    public void addRoleToUser(User user, String roleName) {
        if (user == null) return;

        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        if (user.getRoles() == null) user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        // Ensure matching domain entity exists
        createDomainEntityIfMissing(savedUser, roleName);
    }

    public void removeRoleFromUser(User user, String roleName) {
        if (user == null || user.getRoles() == null) return;
        user.getRoles().removeIf(r -> r.getName().equalsIgnoreCase(roleName));
        userRepository.save(user);
    }

    public List<User> getUsersByRole(String roleName) {
        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role == null || role.getUsers() == null) return List.of();
        return new ArrayList<>(role.getUsers());
    }

    public List<User> getAllStudents() { return getUsersByRole("STUDENT"); }
    public List<User> getAllTeachers() { return getUsersByRole("TEACHER"); }
    public List<User> getAllParents() { return getUsersByRole("PARENT"); }
    public List<User> getAllAdmins() { return getUsersByRole("ADMIN"); }

    // =================== SAVE USERS ===================
    public User saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public User saveUserWithRole(User user, String roleName) {

        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        if (user.getRoles() == null) user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        User savedUser = saveUser(user);

        // 🔥 UPDATED: Safe entity creation (no duplicates)
        createDomainEntityIfMissing(savedUser, roleName);

        return savedUser;
    }

    public User addStudentUser(User user) {
        return saveUserWithRole(user, "STUDENT");
    }

    // =================== PASSWORD RESET ===================
    public User resetPassword(String username, String newPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with username: " + username);
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    // =================== DOMAIN ENTITY ENFORCER ===================
    private void createDomainEntityIfMissing(User user, String roleName) {

        switch (roleName.toUpperCase()) {

            case "STUDENT" -> {
                if (studentRepository.findByUser(user).isEmpty()) {
                    Student student = new Student();
                    student.setUser(user);
                    studentRepository.save(student);
                }
            }

            case "TEACHER" -> {
                if (teacherRepository.findByUser(user).isEmpty()) {
                    Teacher teacher = new Teacher();
                    teacher.setUser(user);
                    teacherRepository.save(teacher);
                }
            }

            case "PARENT" -> {
                if (parentRepository.findByUser(user).isEmpty()) {
                    Parent parent = new Parent();
                    parent.setUser(user);
                    parentRepository.save(parent);
                }
            }

            case "ADMIN" -> {
                if (adminRepository.findByUser(user).isEmpty()) {
                    Admin admin = new Admin();
                    admin.setUser(user);
                    adminRepository.save(admin);
                }
            }
        }
    }
}
