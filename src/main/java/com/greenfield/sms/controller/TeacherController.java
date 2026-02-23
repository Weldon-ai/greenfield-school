package com.greenfield.sms.controller;

import java.security.Principal;
import java.util.List;
import com.greenfield.sms.model.*; 
import com.greenfield.sms.service.*;
import com.greenfield.sms.repository.ClassesRepository;
import com.greenfield.sms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired private UserService userService;
    @Autowired private AssignmentService assignmentService;
    @Autowired private ReportCardService reportCardService;
    @Autowired private MessageService messageService;      
    @Autowired private NotificationService notificationService;
    @Autowired private UserRepository userRepository;
    @Autowired private ClassesRepository classesRepository;

    // ... (Reports, Analytics, Students methods stay as they are)

    // --- NOTIFICATION METHODS ---

    /**
     * Lists all notifications for the logged-in teacher.
     */
    @GetMapping("/notifications")
    public String listNotifications(Model model, Principal principal) {
        // Find teacher using principal to ensure we get the correct user session
        User currentTeacher = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        List<Notification> notifications = notificationService.getAllForUser(currentTeacher);
        
        model.addAttribute("notifications", notifications);
        model.addAttribute("currentUsername", principal.getName());
        return "teacher/notifications";
    }

    /**
     * Marks a notification as read and refreshes the list.
     */
    @GetMapping("/notifications/view/{id}")
    public String viewNotification(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes) {
        // Find the notification
        Notification notification = notificationService.getById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        // Mark as read
        notificationService.markAsRead(id);
        
        // Use a Redirect instead of returning a view directly.
        // This prevents the "null property" error because 'listNotifications' 
        // will re-populate the model correctly.
        return "redirect:/teacher/notifications";
    }
}