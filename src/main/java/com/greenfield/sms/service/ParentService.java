package com.greenfield.sms.service;

import com.greenfield.sms.model.Parent;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.User;
import com.greenfield.sms.repository.ParentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ParentService {

    private final ParentRepository parentRepository;

    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    // ===== Save or update a parent =====
    public Parent saveParent(Parent parent) {
        if (parent.getStudents() == null) {
            parent.setStudents(new HashSet<>());
        }
        return parentRepository.save(parent);
    }

    // ===== Get parent by ID =====
    public Optional<Parent> getParentById(Long id) {
        return parentRepository.findById(id);
    }

    // ===== Get parent by User ID =====
    public Optional<Parent> getParentByUserId(Long userId) {
        return parentRepository.findByUserId(userId);
    }

    // ===== Get parent by username =====
    public Optional<Parent> getParentByUsername(String username) {
        return parentRepository.findByUserUsername(username);
    }

    // ===== Add a student to parent =====
    public Parent addStudentToParent(Parent parent, Student student) {
        if (parent.getStudents() == null) {
            parent.setStudents(new HashSet<>());
        }
        parent.getStudents().add(student);
        return parentRepository.save(parent);
    }

    // ===== Remove a student from parent =====
    public Parent removeStudentFromParent(Parent parent, Student student) {
        if (parent.getStudents() != null) {
            parent.getStudents().remove(student);
        }
        return parentRepository.save(parent);
    }
}
