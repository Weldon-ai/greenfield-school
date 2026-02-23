
package com.greenfield.sms.repository;

import com.greenfield.sms.model.Notification;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ================= USER NOTIFICATIONS (Teachers/Admins/Students) =================
    // Matches the 'private User user' field in your Notification model
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByUserAndReadStatusFalseOrderByCreatedAtDesc(User user);

    // ================= STUDENT SPECIFIC =================
    // Matches 'private Student student'
    List<Notification> findByStudentOrderByCreatedAtDesc(Student student);

    List<Notification> findByStudentAndReadStatusFalseOrderByCreatedAtDesc(Student student);

    // ================= ADMIN / GLOBAL =================
    // Matches 'private boolean adminNotification'
    List<Notification> findByAdminNotificationTrueOrderByCreatedAtDesc();

    List<Notification> findByAdminNotificationTrueAndReadStatusFalseOrderByCreatedAtDesc();
    // NotificationRepository.java
List<Notification> findAllByOrderByCreatedAtDesc(); // Admin view
}