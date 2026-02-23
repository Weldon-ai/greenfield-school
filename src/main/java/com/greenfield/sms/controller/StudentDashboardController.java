package com.greenfield.sms.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentDashboardController {

    @GetMapping("/student/dashboard") // always include leading slash
    public String dashboard(Model model) {
        // Get logged-in user's name
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // this will be the username

        // Pass to Thymeleaf template
        model.addAttribute("studentName", username);

        return "student/student-dashboard"; // templates/student/student-dashboard.html
    }
}
