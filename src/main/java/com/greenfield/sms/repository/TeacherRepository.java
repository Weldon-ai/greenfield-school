package com.greenfield.sms.repository;

import com.greenfield.sms.model.Teacher;
import com.greenfield.sms.model.TeacherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.greenfield.sms.model.User;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /** Fetch teacher by login username */
    Optional<Teacher> findByUsername(String username);

    /** Fetch all teachers with a specific status */
    List<Teacher> findByStatus(TeacherStatus status);

    /** Fetch all teachers who are enabled */
    List<Teacher> findByEnabledTrue();
Optional<Teacher> findByEmail(String email);
    /** Fetch all teachers who are disabled */
    List<Teacher> findByEnabledFalse();
    Optional<Teacher> findByUser(User user);
}
