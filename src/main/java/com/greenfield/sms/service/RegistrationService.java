package com.greenfield.sms.service;

import com.greenfield.sms.model.*;
import com.greenfield.sms.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository,
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

    @Transactional
    public void register(String username,
                         String password,
                         String fullName,
                         String email,
                         String roleName) {

        // ✅ Check for duplicates
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        // Normalize role name
        String roleKey = roleName.startsWith("ROLE_")
                ? roleName.toUpperCase()
                : "ROLE_" + roleName.toUpperCase();

        // Find or create role
        Role role = roleRepository.findByName(roleKey)
                .orElseGet(() -> roleRepository.save(new Role(roleKey)));

        // Create user
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singleton(role));
        user.setEnabled(true);

        userRepository.save(user);

        // 🔥 Create the domain entity based on role
        createDomainEntity(user, roleKey);
    }

    // =================== PRIVATE HELPER ===================
    private void createDomainEntity(User user, String roleKey) {
        switch (roleKey) {

            case "ROLE_STUDENT" -> {
                if (studentRepository.findByUser(user).isEmpty()) {
                    Student student = new Student();
                    student.setUser(user);
                    student.setStatus(StudentStatus.ACTIVE);
                    student.setEnabled(true);

                    // Set dateJoined using LocalDate (matches Student entity)
                    student.setDateJoined(LocalDate.now());

                    // Generate unique admission number
                    student.setAdmissionNumber(generateAdmissionNumber(user));

                    studentRepository.save(student);
                }
            }

            case "ROLE_TEACHER" -> {
                if (teacherRepository.findByUser(user).isEmpty()) {
                    Teacher teacher = new Teacher();
                    teacher.setUser(user);
                    teacher.setStatus(TeacherStatus.ACTIVE);
                    teacher.setEnabled(true);
                    teacherRepository.save(teacher);
                }
            }

            case "ROLE_PARENT" -> {
                if (parentRepository.findByUser(user).isEmpty()) {
                    Parent parent = new Parent();
                    parent.setUser(user);
                    parentRepository.save(parent);
                }
            }

            case "ROLE_ADMIN" -> {
                if (adminRepository.findByUser(user).isEmpty()) {
                    Admin admin = new Admin();
                    admin.setUser(user);
                    adminRepository.save(admin);
                }
            }

            default -> throw new RuntimeException("Unsupported role: " + roleKey);
        }
    }

    // =================== HELPER TO GENERATE UNIQUE ADMISSION NUMBER ===================
    private String generateAdmissionNumber(User user) {
        // Example: STU-YYYYMMDD-HHMMSS-userId
        String datePart = java.time.LocalDate.now().toString().replace("-", "");
        return "STU-" + datePart + "-" + user.getId();
    }
}
