package com.greenfield.sms.repository;

import com.greenfield.sms.model.SchemeOfWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchemeOfWorkRepository extends JpaRepository<SchemeOfWork, Long> {

    // 1. Find a specific scheme by its professional reference number (for printing)
    Optional<SchemeOfWork> findByReferenceNumber(String referenceNumber);

    // 2. Filter schemes for a specific teacher (so they don't see others' work)
    List<SchemeOfWork> findByTeacherNameOrderByCreatedAtDesc(String teacherName);

    // 3. Filter by Class and Subject (useful for departmental reviews)
    List<SchemeOfWork> findByTargetClassAndSubject(String targetClass, String subject);

    // 4. Find all pending schemes (for the Principal/HOD dashboard)
    List<SchemeOfWork> findByStatus(String status);
    
    // 5. Search for a topic using a keyword
    List<SchemeOfWork> findByTopicContainingIgnoreCase(String keyword);
}