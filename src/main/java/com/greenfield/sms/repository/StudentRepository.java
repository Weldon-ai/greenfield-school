package com.greenfield.sms.repository;

import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.StudentStatus;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // ======================================================
    // 🔥 PERFORMANCE: FETCH JOIN to avoid N+1 problem
    // ======================================================
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user LEFT JOIN FETCH s.classes")
    List<Student> findAllWithDetails();

    // ======================================================
    // IDENTITY MAPPINGS
    // ======================================================
    Optional<Student> findByUserUsername(String username);

    Optional<Student> findByAdmissionNumber(String admissionNumber);

    boolean existsByAdmissionNumber(String admissionNumber);

    boolean existsByUser(User user);

    boolean existsByUserUsername(String username);

    boolean existsByUserId(Long userId);

    @Query("SELECT s FROM Student s JOIN FETCH s.user WHERE s.user.username = :username")
    Optional<Student> findWithUserByUsername(@Param("username") String username);

    // ======================================================
    // STATUS & ADMINISTRATIVE QUERIES
    // ======================================================
    List<Student> findByEnabledTrue();

    List<Student> findByStatus(StudentStatus status);

    long countByStatus(StudentStatus status);

    long countByClasses(Classes classes);

    // ======================================================
    // CLASS & TEACHER QUERIES
    // ======================================================
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.classes.id = :classId")
    List<Student> findByClasses_Id(@Param("classId") Long classId);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.user WHERE s.classes.id = :classId AND s.status = :status")
    List<Student> findByClasses_IdAndStatus(@Param("classId") Long classId, @Param("status") StudentStatus status);

    List<Student> findByClasses_Teacher_Id(Long teacherId);

    List<Student> findBySubjects_Id(Long subjectId);

    Optional<Student> findByUser(User user);

    // ======================================================
    // ✅ FIXED: Find students by Classes object
    // ======================================================
    List<Student> findByClasses(Classes classes);

    // ======================================================
    // ✅ FIXED: Update student status
    // ======================================================
    @Modifying
    @Query("UPDATE Student s SET s.status = :status WHERE s.id = :studentId")
    void updateStatus(@Param("studentId") Long studentId, @Param("status") StudentStatus status);
}