package com.greenfield.sms.repository; // Repository package

import com.greenfield.sms.model.ClassSubject; // Mapping entity
import org.springframework.data.jpa.repository.JpaRepository; // JPA repository

public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long> {
    // CRUD handled automatically
}
