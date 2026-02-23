package com.greenfield.sms.controller;

import com.greenfield.sms.model.Message;
import com.greenfield.sms.model.Notification;
import com.greenfield.sms.model.User;
import com.greenfield.sms.service.MessageService;
import com.greenfield.sms.service.UserService;
import com.greenfield.sms.service.NotificationService;
import com.greenfield.sms.repository.UserRepository; // Added
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class MessageTeacherController {

    private final MessageService messageService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final UserRepository userRepository; // Added to ensure DB lookups are solid

    public MessageTeacherController(MessageService messageService, 
                                    UserService userService, 
                                    NotificationService notificationService,
                                    UserRepository userRepository) {
        this.messageService = messageService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    /**
     * SOLUTION FOR 500 ERROR:
     * Instead of redirecting, we accept both paths. 
     * If the user hits /teacher/messages, we manually call the inbox logic.
     */
    @GetMapping("/messages")
    public String handleOldLink(Model model, Principal principal) {
        return teacherMessages(model, principal);
    }

    // ===== Dashboard / Inbox =====
    @GetMapping("/messaging") 
    public String teacherMessages(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        // Fetch user from repository directly to ensure we have the latest entity
        User teacher = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        List<Message> userMessages = messageService.getMessagesForUser(teacher);
        List<Notification> allNotifications = notificationService.getAllForUser(teacher);
        
        model.addAttribute("messages", userMessages);
        model.addAttribute("users", userService.findAll()); 
        model.addAttribute("notifications", allNotifications);
        model.addAttribute("message", new Message()); 

        // This path MUST exist: src/main/resources/templates/teacher/messages.html
        return "teacher/messages"; 
    }

    // ===== Send message =====
    @PostMapping("/messaging/send")
    public String sendMessage(@RequestParam("receiverId") Long receiverId,
                              @ModelAttribute("message") Message message,
                              Principal principal,
                              RedirectAttributes ra) {

        User sender = userRepository.findByUsername(principal.getName()).orElseThrow();
        User receiver = userService.getById(receiverId)
                        .orElseThrow(() -> new RuntimeException("Recipient not found"));

        messageService.sendMessage(
                message.getSubject(),
                message.getBody(),
                sender,
                receiver
        );
        
        ra.addFlashAttribute("success", "Message sent successfully!");
        return "redirect:/teacher/messaging"; 
    }

    // ===== View single message =====
    @GetMapping("/messaging/view/{id}")
    public String viewMessage(@PathVariable Long id, Model model) {
        Message msg = messageService.getById(id);
        if (msg != null) {
            messageService.markAsRead(msg);
            model.addAttribute("message", msg);
            return "teacher/message-view"; 
        }
        return "redirect:/teacher/messaging";
    }
}