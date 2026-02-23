package com.greenfield.sms.repository;

import com.greenfield.sms.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // FIX: Change findBySubjectName to findByName
    Subject findByName(String name);

    // This stays the same (links to the Set<Classes> classes field)
    List<Subject> findByClasses_Id(Long classId);
    
    List<Subject> findByTeacherIdAndClasses_Id(Long teacherId, Long classId);
}