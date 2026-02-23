package com.greenfield.sms.service;

import com.greenfield.sms.model.Admin;
import com.greenfield.sms.model.Role;
import com.greenfield.sms.model.User;
import com.greenfield.sms.repository.AdminRepository;
import com.greenfield.sms.repository.RoleRepository;
import com.greenfield.sms.repository.UserRepository;
import com.greenfield.sms.repository.StudentRepository;
import com.greenfield.sms.repository.TeacherRepository;

import com.greenfield.sms.repository.FeeRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    
    private final FeeRepository feeRepository;

    public AdminService(AdminRepository adminRepository,
                        UserRepository userRepository,
                        RoleRepository roleRepository,
                        StudentRepository studentRepository,
                        TeacherRepository teacherRepository,
                       
                        FeeRepository feeRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        
        this.feeRepository = feeRepository;
    }

    // ===== Create new Admin =====
    public Admin createAdmin(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

        if (user.getRoles() == null) user.setRoles(Set.of(adminRole));
        else user.getRoles().add(adminRole);

        userRepository.save(user); // save/update user with role

        Admin admin = new Admin(user);
        return adminRepository.save(admin);
    }

    // ===== Get admin by ID =====
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    // ===== Get admin by linked user ID =====
    public Optional<Admin> getAdminByUserId(Long userId) {
        return adminRepository.findByUserId(userId);
    }

    // ===== Delete admin =====
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    // ===== Update admin =====
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // ==================== DASHBOARD METRICS ====================

    // Total number of students
    public long getTotalStudents() {
        return studentRepository.count();
    }

    // Total number of teachers
    public long getTotalTeachers() {
        return teacherRepository.count();
    }

    // Total number of active classes
    

    // Monthly revenue (placeholder)
    public String getMonthlyRevenue() {
        // TODO: Replace with actual revenue calculation logic
        return "$0";
    }

    // Pending fees (placeholder)
    public String getPendingFees() {
        // TODO: Replace with actual pending fees logic
        return "$0";
    }
}
