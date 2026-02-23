package com.greenfield.sms.service;

import com.greenfield.sms.model.*;
import com.greenfield.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ClassesRepository classesRepository;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          ClassesRepository classesRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.classesRepository = classesRepository;
    }

    // =================== ADD NEW STUDENT ===================
    @Transactional
    public Student addNewStudent(Student student) {
        if (student == null || student.getUser() == null) {
            throw new IllegalArgumentException("Student and linked User must not be null.");
        }

        User user = student.getUser();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Save User first
        User savedUser = userRepository.save(user);
        student.setUser(savedUser);

        if (student.getAdmissionNumber() == null || student.getAdmissionNumber().isEmpty()) {
            throw new RuntimeException("Admission Number is required to save a student.");
        }

        student.setStatus(StudentStatus.ACTIVE);

        return studentRepository.save(student);
    }

    // =================== BASIC CRUD ===================
    public List<Student> getAllStudents() {
        // Use join fetch to avoid N+1 problem
        return studentRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    public Optional<Student> getById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    // =================== CLASS MANAGEMENT ===================
    @Transactional
    public void assignToClass(Long studentId, Long classId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Classes clazz = classesRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        student.setClasses(clazz);
        studentRepository.save(student);
    }

    @Transactional
    public void promoteStudent(Long studentId, Long nextClassId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Classes nextClass = classesRepository.findById(nextClassId)
                .orElseThrow(() -> new RuntimeException("Target class not found"));

        student.setClasses(nextClass); // Updated promotion logic
        studentRepository.save(student);
    }

    // =================== QUERY METHODS ===================
    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findByClasses_Id(classId);
    }

    public List<Student> getStudentsByClasses(Classes classes) {
        // Use repository query to fetch students by Classes object
        return studentRepository.findByClasses(classes);
    }

    public List<Student> getByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status);
    }

    public long countStudents() {
        return studentRepository.count();
    }

    // =================== STATUS MANAGEMENT ===================
    @Transactional
    public void updateStatus(Long studentId, StudentStatus status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setStatus(status);
        studentRepository.save(student);
    }
public List<Student> getStudentsByClassAndStatus(Long classId, StudentStatus status) {
    return studentRepository.findByClasses_IdAndStatus(classId, status);
}
}