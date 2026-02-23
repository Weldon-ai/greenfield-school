package com.greenfield.sms.repository;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Exam;
import com.greenfield.sms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    // ================= SINGLE CLASS METHODS =================

    // Get all exams for a specific class
    List<Exam> findByClasses(Classes classes);

    // Fetch a specific exam by ID along with its class
    Exam findByIdAndClasses(Long id, Classes classes);

    // Fetch exams for a class within a date range
    @Query("SELECT e FROM Exam e WHERE e.classes = :classes AND e.examDate BETWEEN :startDate AND :endDate")
    List<Exam> findByClassAndDateRange(@Param("classes") Classes classes,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    // Fetch exams that are not locked yet (open for teachers)
    @Query("SELECT e FROM Exam e WHERE e.locked = false AND e.classes = :classes")
    List<Exam> findOpenExamsByClass(@Param("classes") Classes classes);

    // Fetch exams for export within a term and class
    @Query("SELECT e FROM Exam e WHERE e.term = :term AND e.classes = :classes")
    List<Exam> findByTermAndClass(@Param("term") String term, @Param("classes") Classes classes);

    // Fetch exams with results count (for analytics)
    @Query("SELECT e, SIZE(e.results) FROM Exam e WHERE e.classes = :classes")
    List<Object[]> findExamsWithResultCount(@Param("classes") Classes classes);

    // ================= MULTIPLE CLASSES METHODS =================

    // Fetch exams for multiple classes
    @Query("SELECT e FROM Exam e WHERE e.classes IN :classes")
    List<Exam> findByClassesIn(@Param("classes") List<Classes> classes);

    // ================= STUDENT / RESULT METHODS =================

    // Fetch exams where a specific student has results
    List<Exam> findByResults_Student(Student student);

    // Fetch results for export (all results of a given exam)
    @Query("SELECT r FROM Exam e JOIN e.results r WHERE e.id = :examId")
    List<Exam.ExamResult> findResultsByExamId(@Param("examId") Long examId);

    // Fetch results for a specific student across exams
    @Query("SELECT r FROM Exam e JOIN e.results r WHERE r.student = :student")
    List<Exam.ExamResult> findResultsByStudent(@Param("student") Student student);

    // Fetch top N students per exam
    @Query("SELECT r FROM Exam e JOIN e.results r WHERE e.id = :examId ORDER BY r.marks DESC")
    List<Exam.ExamResult> findTopResultsByExamId(@Param("examId") Long examId);

    // ================= TERM & LOCKED FILTER METHODS =================

    // Fetch exams by term (e.g., "Term 1", "Term 2")
    List<Exam> findByTerm(String term);

    // Fetch exams that are locked/unlocked
    List<Exam> findByLockedTrue();
    List<Exam> findByLockedFalse();
}
