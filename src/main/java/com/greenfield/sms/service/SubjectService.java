package com.greenfield.sms.service;

import com.greenfield.sms.model.Subject;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Teacher;
import com.greenfield.sms.repository.SubjectRepository;
import com.greenfield.sms.repository.ClassesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ClassesRepository classesRepository;
    private final TeacherService teacherService;

    public SubjectService(SubjectRepository subjectRepository,
                          ClassesRepository classesRepository,
                          TeacherService teacherService) {
        this.subjectRepository = subjectRepository;
        this.classesRepository = classesRepository;
        this.teacherService = teacherService;
    }

    // ================= BASIC OPERATIONS =================

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getById(Long id) {
        return subjectRepository.findById(id);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    // ================= ASSIGNMENTS (MANY-TO-MANY LOGIC) =================

    /**
     * BULK UPDATE: This is what handles the checkboxes.
     * It clears the previous curriculum and replaces it with the new selection.
     */
    @Transactional
    public void updateSubjectClasses(Long subjectId, List<Long> classIds) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        
        // 1. Clear current set
        subject.getClasses().clear();
        
        // 2. Add selected classes
        if (classIds != null && !classIds.isEmpty()) {
            List<Classes> selectedClasses = classesRepository.findAllById(classIds);
            subject.getClasses().addAll(selectedClasses);
        }
        
        // 3. Save and Flush forces the 'subject_classes' table to update NOW
        subjectRepository.saveAndFlush(subject);
    }

    @Transactional
    public void assignClass(Long subjectId, Long classId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Classes cls = classesRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        subject.getClasses().add(cls);
        subjectRepository.saveAndFlush(subject);
    }

    @Transactional
    public void removeClass(Long subjectId, Long classId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Classes cls = classesRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        subject.getClasses().remove(cls);
        subjectRepository.saveAndFlush(subject);
    }

    // ================= TEACHER MANAGEMENT =================

    @Transactional
    public void assignTeacher(Long subjectId, Long teacherId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Teacher teacher = teacherService.getTeacherById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        subject.setTeacher(teacher); 
        subjectRepository.saveAndFlush(subject);
    }
}