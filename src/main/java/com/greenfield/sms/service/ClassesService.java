package com.greenfield.sms.service;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.ClassLevel;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.repository.ClassesRepository;
import com.greenfield.sms.repository.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClassesService {

    private final ClassesRepository classesRepository;
    private final StudentRepository studentRepository; // Required for individual student updates

    public ClassesService(ClassesRepository classesRepository, StudentRepository studentRepository) {
        this.classesRepository = classesRepository;
        this.studentRepository = studentRepository;
    }

    // ================= CRUD METHODS =================

    public List<Classes> getAllClasses() {
        return classesRepository.findAll();
    }

    /** * Core method used by AdminClassController 
     */
    public Optional<Classes> getById(Long id) {
        return classesRepository.findById(id);
    }

    public List<Classes> getAllClassesOrdered() {
        return classesRepository.findAll(Sort.by(Sort.Direction.ASC, "className"));
    }

    public Classes saveClass(Classes classes) {
        return classesRepository.save(classes);
    }

    public void deleteById(Long id) {
        classesRepository.deleteById(id);
    }
public Classes getClassById(Long id) {
    return classesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
}

    // ================= STUDENT MANAGEMENT LOGIC =================

    @Transactional
    public void promoteStudents(Classes currentClass) {
        Optional<Classes> nextClassOpt = getNextClass(currentClass);
        if (nextClassOpt.isEmpty()) return;

        Classes nextClass = nextClassOpt.get();
        Set<Student> students = currentClass.getStudents();

        for (Student student : students) {
            student.setClasses(nextClass);
        }
        // Saving the students directly or via repo is safer than relying on class cascade only
        studentRepository.saveAll(students); 
    }
public long countClasses() {
    return classesRepository.count();
}
    @Transactional
    public void retainStudent(Student student, Classes targetClass) {
        // Retention logic usually keeps them in the current class or re-assigns
        student.setClasses(targetClass);
        studentRepository.save(student);
    }

    @Transactional
    public void deferStudent(Student student, Classes targetClass) {
        // Custom logic for deferment: often involves a status change or nulling the class
        student.setClasses(null); 
        // student.setStatus("DEFERRED"); // If you have a status field
        studentRepository.save(student);
    }

    @Transactional
    public void removeStudentFromClass(Student student, Classes targetClass) {
        if (student.getClasses() != null && student.getClasses().getId().equals(targetClass.getId())) {
            student.setClasses(null);
            studentRepository.save(student);
        }
    }

    // ================= HIERARCHY HELPERS =================

    public Optional<Classes> getNextClass(Classes currentClass) {
        ClassLevel currentLevel = currentClass.getClassLevel();
        if (currentLevel == null) return Optional.empty();
        
        int nextOrdinal = currentLevel.ordinal() + 1;
        if (nextOrdinal >= ClassLevel.values().length) return Optional.empty();

        ClassLevel nextLevel = ClassLevel.values()[nextOrdinal];
        return classesRepository.findByClassLevel(nextLevel);
    }

    public Optional<Classes> findByClassLevel(ClassLevel level) {
        return classesRepository.findByClassLevel(level);
    }
}