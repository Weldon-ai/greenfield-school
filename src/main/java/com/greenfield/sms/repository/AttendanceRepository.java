package com.greenfield.sms.repository;

import com.greenfield.sms.model.Attendance;
import com.greenfield.sms.model.Classes; // Updated
import com.greenfield.sms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // ================= STUDENT / TEACHER =================

    /** Fetch all attendance records for a student */
    List<Attendance> findByStudent(Student student);

    /** * Fetch attendance for a student in a class on a specific date.
     * Updated: SchoolClass -> Classes
     */
    Optional<Attendance> findByStudentAndClassesAndDate(
            Student student,
            Classes classes,
            LocalDate date
    );

    // ================= ADMIN DASHBOARD SUPPORT =================

    /** * Fetch attendance for a class within a date range.
     * Updated: SchoolClass -> Classes
     */
    List<Attendance> findByClassesAndDateBetween(
            Classes classes,
            LocalDate startDate,
            LocalDate endDate
    );

    /** * Fetch all attendance records for a class.
     * Updated: SchoolClass -> Classes
     */
    List<Attendance> findByClasses(Classes classes);

    /** Fetch attendance records between dates */
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);

    /** Fetch attendance for a specific date */
    List<Attendance> findByDate(LocalDate date);

    // ================= AUTO-LOCK SUPPORT =================

    /** Fetch all attendance records that are NOT locked */
    List<Attendance> findByLockedFalse();

    // ================= REPORTS & EXPORT =================

    /** Custom query: Fetch a student's attendance between two dates */
    @Query("""
        SELECT a
        FROM Attendance a
        WHERE a.student = :student
          AND a.date BETWEEN :startDate AND :endDate
    """)
    List<Attendance> findStudentAttendanceBetweenDates(
            @Param("student") Student student,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    List<Attendance> findByLockedFalseAndDateBefore(LocalDate date);
}