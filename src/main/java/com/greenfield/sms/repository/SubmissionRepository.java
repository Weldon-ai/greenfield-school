package com.greenfield.sms.repository;

import com.greenfield.sms.model.Assignment;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Find all submissions made by a specific student
    List<Submission> findByStudent(Student student);

    // Find all submissions for a specific assignment (for the teacher's grading list)
    List<Submission> findByAssignment(Assignment assignment);

    // Find a specific submission to check if a student has already submitted (prevents duplicates)
    Optional<Submission> findByStudentAndAssignment(Student student, Assignment assignment);

    // ============================
    // NEW UTILITY QUERIES
    // ============================

    /**
     * Count how many students have submitted a specific assignment.
     * Useful for showing "15/30 submitted" on the teacher's dashboard.
     */
    long countByAssignment(Assignment assignment);

    /**
     * Find submissions for a specific assignment that haven't been graded yet.
     */
    @Query("SELECT s FROM Submission s WHERE s.assignment = :assignment AND s.marks IS NULL")
    List<Submission> findUngradedSubmissions(@Param("assignment") Assignment assignment);
}