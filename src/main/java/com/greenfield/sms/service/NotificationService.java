package com.greenfield.sms.service;

import com.greenfield.sms.model.Notification;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.User;
import com.greenfield.sms.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // ================= USER NOTIFICATIONS =================
    /**
     * Fetch all notifications for a user (admin/student/teacher).
     * Returns empty list if user is null.
     */
    public List<Notification> getAllForUser(User user) {
        if (user == null) return new ArrayList<>();
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Fetch unread notifications for a user.
     */
    public List<Notification> getUnreadForUser(User user) {
        if (user == null) return new ArrayList<>();
        return notificationRepository.findByUserAndReadStatusFalseOrderByCreatedAtDesc(user);
    }

    /**
     * Fetch a notification by ID.
     */
    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    // ================= STUDENT NOTIFICATIONS =================
    /**
     * Fetch notifications for a specific student.
     */
    public List<Notification> getNotificationsForStudent(Student student) {
        if (student == null) return new ArrayList<>();
        return notificationRepository.findByStudentOrderByCreatedAtDesc(student);
    }

    // ================= ADMIN NOTIFICATIONS =================
    /**
     * Fetch all notifications for admin view.
     * This replaces getAllAdminNotifications() in controllers.
     */
    public List<Notification> getAllAdminNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Save or create a notification for any user.
     * Replaces saveNotification() in controllers.
     */
    @Transactional
    public Notification saveNotification(Notification notification) {
        if (notification == null) return null;
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        if (notification.getReadStatus() == null) {
            notification.setReadStatus(false);
        }
        return notificationRepository.save(notification);
    }

    /**
     * Delete a notification by ID.
     */
    @Transactional
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    // ================= NOTIFICATION ACTIONS =================
    /**
     * Mark a single notification as read.
     */
    @Transactional
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> n.setReadStatus(true));
    }

    /**
     * Mark all notifications as read for a specific user.
     */
    @Transactional
    public void markAllAsReadForUser(User user) {
        List<Notification> unread = notificationRepository.findByUserAndReadStatusFalseOrderByCreatedAtDesc(user);
        unread.forEach(n -> n.setReadStatus(true));
    }
    
}