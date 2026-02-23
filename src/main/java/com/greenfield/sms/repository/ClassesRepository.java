package com.greenfield.sms.repository;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.ClassLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {

    // ================= BASIC QUERIES =================

    /** * Fetch all classes assigned to a specific teacher 
     */
    List<Classes> findByTeacherId(Long teacherId);

    /** * Fetch all classes for a teacher ordered by class level 
     */
    List<Classes> findByTeacherIdOrderByClassLevelAsc(Long teacherId);

    /** * Find a class by its unique class code (e.g., PG, G1, G9) 
     */
    Optional<Classes> findByClassCode(String classCode);

    /** * Find a class by its ClassLevel enum (used for promotion/demotion) 
     */
    Optional<Classes> findByClassLevel(ClassLevel classLevel);

    /** * Fetch all classes ordered by class level ascending 
     */
    List<Classes> findAllByOrderByClassLevelAsc();

    // ================= ADDITIONAL QUERIES =================

    /** * Check if a class exists by its name 
     */
    boolean existsByClassName(String className);

    /** * Updated Query: Uses 's.classes' to match the field name in the Student entity.
     * This ensures the join works correctly with your standardized naming.
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.classes.id = :classId")
    long countStudentsInClass(@Param("classId") Long classId);

    /** * Fetch all classes that have at least one student enrolled 
     */
    @Query("SELECT DISTINCT c FROM Classes c JOIN c.students s ORDER BY c.classLevel ASC")
    List<Classes> findAllWithStudents();

    /** * Search classes by name (Case-insensitive)
     */
    List<Classes> findByClassNameContainingIgnoreCase(String className);
    
}