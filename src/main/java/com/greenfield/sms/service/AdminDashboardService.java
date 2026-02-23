package com.greenfield.sms.service;

import com.greenfield.sms.repository.StudentRepository;
import com.greenfield.sms.repository.TeacherRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminDashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    

    public AdminDashboardService(
            StudentRepository studentRepository,
            TeacherRepository teacherRepository
            ) {

        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        
    }

    // ===== DASHBOARD METRICS =====

    public long getTotalStudents() {
        return studentRepository.count();
    }

    public long getTotalTeachers() {
        return teacherRepository.count();
    }

    

    

    
}
