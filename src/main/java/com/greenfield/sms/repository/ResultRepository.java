package com.greenfield.sms.repository;

import com.greenfield.sms.model.Result;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.Exam;
import com.greenfield.sms.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    // Get all results for a specific student across all exams
    List<Result> findByStudent(Student student);

    // Get all results for a specific exam (useful for class-wide statistics)
    List<Result> findByExam(Exam exam);

    /**
     * Get all results for a specific student in a specific exam.
     * Use this for generating an individual student's report card for one term.
     */
    List<Result> findByStudentAndExam(Student student, Exam exam);

    /**
     * Find a specific result for a student, exam, and subject.
     * Critical for the "addOrUpdate" logic to prevent duplicate marks for the same subject.
     */
    Optional<Result> findByStudentAndExamAndSubject(Student student, Exam exam, Subject subject);

    // Get all results for a specific subject within an exam (e.g., all Math marks for Term 1)
    List<Result> findByExamAndSubject(Exam exam, Subject subject);
}