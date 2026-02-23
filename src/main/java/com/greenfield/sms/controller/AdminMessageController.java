package com.greenfield.sms.controller;

import com.greenfield.sms.model.Message;
import com.greenfield.sms.model.User;
import com.greenfield.sms.service.MessageService;
import com.greenfield.sms.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminMessageController {

    private final MessageService messageService;
    private final UserService userService;

    public AdminMessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/admin/messages")
    public String allMessages(Model model) {
        // ✅ Get current admin
        User admin = userService.getCurrentUser(); // use getCurrentUser() from UserService

        // ✅ Fetch inbox and sent messages using the updated method names
        List<Message> inbox = messageService.getInbox(admin);
        List<Message> sent = messageService.getSent(admin);

        model.addAttribute("inbox", inbox);
        model.addAttribute("sent", sent);

        return "admin/messages"; // points to admin/messages.html template
    }
}
