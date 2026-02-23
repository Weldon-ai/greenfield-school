package com.greenfield.sms.service;

import com.greenfield.sms.model.Assignment;
import com.greenfield.sms.model.AssignmentStatus;
import com.greenfield.sms.model.Classes; // ✅ Updated import
import com.greenfield.sms.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    // =========================
    // STUDENT SIDE
    // =========================

    /**
     * Get a single assignment by ID
     */
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    /**
     * UPDATED: Changed parameter type to Classes
     */
    public List<Assignment> getAssignmentsForClass(Classes classes) {
        return assignmentRepository.findByClasses(classes);
    }

    // =========================
    // TEACHER SIDE
    // =========================

    /**
     * Get all assignments (teacher overview)
     */
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    /**
     * UPDATED: Changed parameter type to Classes
     */
    public List<Assignment> getAssignmentsForClassByStatus(
            Classes classes,
            AssignmentStatus status
    ) {
        return assignmentRepository.findByClassesAndStatus(classes, status);
    }

    /**
     * Save or update an assignment
     */
    public Assignment saveAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    /**
     * Delete an assignment by ID
     */
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}