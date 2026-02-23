package com.greenfield.sms.repository;

import com.greenfield.sms.model.Assignment;
import com.greenfield.sms.model.Classes; // ✅ Updated import
import com.greenfield.sms.model.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // ======================
    // STUDENT SIDE
    // ======================
    
    /**
     * UPDATED: Changed parameter type to Classes 
     * Matches the 'classes' field in Assignment.java
     */
    List<Assignment> findByClasses(Classes classes);

    /**
     * UPDATED: Filter by class and status
     */
    List<Assignment> findByClassesAndStatus(Classes classes, AssignmentStatus status);
}