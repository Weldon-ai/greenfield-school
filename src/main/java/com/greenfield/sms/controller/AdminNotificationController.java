package com.greenfield.sms.controller;

import com.greenfield.sms.model.Notification;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.service.NotificationService;
import com.greenfield.sms.service.StudentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/notifications")
public class AdminNotificationController {

    private final NotificationService notificationService;
    private final StudentService studentService;

    public AdminNotificationController(NotificationService notificationService,
                                       StudentService studentService) {
        this.notificationService = notificationService;
        this.studentService = studentService;
    }

    // ================= LIST ALL NOTIFICATIONS =================
    @GetMapping
    public String listNotifications(Model model) {
        List<Notification> notifications = notificationService.getAllAdminNotifications();
        model.addAttribute("notifications", notifications);

        model.addAttribute("newNotification", new Notification());
        model.addAttribute("students", studentService.getAllStudents());

        return "admin/admin-notifications"; // Thymeleaf page
    }

    // ================= CREATE NOTIFICATION =================
    @PostMapping("/create")
    public String createNotification(@ModelAttribute("newNotification") Notification notification,
                                     @RequestParam Long studentId,
                                     RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.getById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            notification.setStudent(student);
            notificationService.saveNotification(notification);
            redirectAttributes.addFlashAttribute("success", "Notification sent successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to send notification: " + e.getMessage());
        }
        return "redirect:/admin/notifications";
    }

    // ================= MARK NOTIFICATION AS READ =================
    @GetMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Correct: pass the ID, not the Notification object
            notificationService.markAsRead(id);
            redirectAttributes.addFlashAttribute("success", "Notification marked as read!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to mark as read: " + e.getMessage());
        }
        return "redirect:/admin/notifications";
    }

    // ================= DELETE NOTIFICATION =================
    @GetMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            notificationService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Notification deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete notification: " + e.getMessage());
        }
        return "redirect:/admin/notifications";
    }
}