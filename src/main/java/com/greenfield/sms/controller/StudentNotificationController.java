package com.greenfield.sms.controller;

import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.User;
import com.greenfield.sms.service.NotificationService;
import com.greenfield.sms.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class StudentNotificationController {

    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    public StudentNotificationController(StudentRepository studentRepository, NotificationService notificationService) {
        this.studentRepository = studentRepository;
        this.notificationService = notificationService;
    }

    @GetMapping("/student/notifications")
    public String viewNotifications(Model model, Principal principal) {

        // 1. Get logged-in student
        Student student = studentRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Fetch notifications
        model.addAttribute("notifications", notificationService.getNotificationsForStudent(student));

        return "student/notifications"; // Thymeleaf template
    }
}
