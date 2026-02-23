package com.greenfield.sms.service;

import com.greenfield.sms.model.Role;
import com.greenfield.sms.model.Teacher;
import com.greenfield.sms.model.TeacherStatus;
import com.greenfield.sms.model.User;
import com.greenfield.sms.repository.RoleRepository;
import com.greenfield.sms.repository.TeacherRepository;
import com.greenfield.sms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // <-- for assigning roles
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===============================
    // BASIC TEACHER OPERATIONS
    // ===============================

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public List<Teacher> getByStatus(TeacherStatus status) {
        return teacherRepository.findAll().stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    // ===============================
    // SAVE / UPDATE TEACHER AND LINK USER WITH ROLE
    // ===============================
    public Teacher saveTeacher(Teacher teacher, String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Admin must provide a password for the teacher.");
        }

        User user = teacher.getUser();
        if (user == null) {
            user = new User();
        }

        // Set user fields
        user.setUsername(teacher.getUsername());
        user.setFullName(teacher.getFullName());
        user.setEmail(teacher.getEmail());
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Assign ROLE_TEACHER
        Role teacherRole = roleRepository.findByName("ROLE_TEACHER")
                .orElseThrow(() -> new RuntimeException("ROLE_TEACHER not found in DB"));
        user.getRoles().add(teacherRole);

        // Save user first
        userRepository.save(user);

        // Link teacher to user
        teacher.setUser(user);
        user.setTeacher(teacher);

        // Default teacher status
        if (teacher.getStatus() == null) {
            teacher.setStatus(TeacherStatus.ACTIVE);
        }
        teacher.setEnabled(true);

        return teacherRepository.save(teacher);
    }

    /** Update existing teacher and optionally update password */
    public Teacher updateTeacher(Teacher teacher, String rawPassword) {
        Teacher existing = getTeacherById(teacher.getId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        // Update teacher fields
        existing.setFirstName(teacher.getFirstName());
        existing.setLastName(teacher.getLastName());
        existing.setUsername(teacher.getUsername());
        existing.setEmail(teacher.getEmail());
        existing.setPhone(teacher.getPhone());
        existing.setGender(teacher.getGender());
        existing.setSubject(teacher.getSubject());

        // Update linked User
        User user = existing.getUser();
        if (user == null) {
            user = new User();
            existing.setUser(user);
            user.setTeacher(existing);
        }
        user.setUsername(existing.getUsername());
        user.setEmail(existing.getEmail());
        user.setFullName(existing.getFullName());
        user.setEnabled(true);

        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        // Ensure ROLE_TEACHER is assigned
        Role teacherRole = roleRepository.findByName("ROLE_TEACHER")
                .orElseThrow(() -> new RuntimeException("ROLE_TEACHER not found in DB"));
        user.getRoles().add(teacherRole);

        userRepository.save(user);
        return teacherRepository.save(existing);
    }

    // ===============================
    // DELETE TEACHER AND LINKED USER
    // ===============================
    public void deleteTeacher(Long id) {
        teacherRepository.findById(id).ifPresent(teacher -> {
            User user = teacher.getUser();
            if (user != null) userRepository.delete(user);
            teacherRepository.delete(teacher);
        });
    }

    // ===============================
    // ADMIN STATUS MANAGEMENT
    // ===============================
    private void updateTeacherStatus(Teacher teacher, TeacherStatus status, boolean enabled) {
        teacher.setStatus(status);
        teacher.setEnabled(enabled);
        teacherRepository.save(teacher);
    }
// Add this inside TeacherService.java
public Optional<Teacher> getByEmail(String email) {
    return teacherRepository.findByEmail(email);
}
    public void enableTeacher(Long teacherId) {
        getTeacherById(teacherId).ifPresent(t -> updateTeacherStatus(t, TeacherStatus.ACTIVE, true));
    }

    public void disableTeacher(Long teacherId) {
        getTeacherById(teacherId).ifPresent(t -> updateTeacherStatus(t, TeacherStatus.DISABLED, false));
    }

    public void suspendTeacher(Long teacherId) {
        getTeacherById(teacherId).ifPresent(t -> updateTeacherStatus(t, TeacherStatus.SUSPENDED, true));
    }

    public void retireTeacher(Long teacherId) {
        getTeacherById(teacherId).ifPresent(t -> updateTeacherStatus(t, TeacherStatus.RETIRED, false));
    }
public long countTeachers() {
    return teacherRepository.count();
}
    public void terminateTeacher(Long teacherId) {
        getTeacherById(teacherId).ifPresent(t -> updateTeacherStatus(t, TeacherStatus.TERMINATED, false));
    }
}
