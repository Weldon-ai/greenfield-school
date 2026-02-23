package com.greenfield.sms.repository;

import com.greenfield.sms.model.Fee;
import com.greenfield.sms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    // Get all fees for a student
    List<Fee> findByStudentOrderByDueDateDesc(Student student);
}
