package com.greenfield.sms.controller;

import com.greenfield.sms.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String fullName,
                               @RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               @RequestParam(required = false) String role,
                               Model model) {

        if (role == null || role.isBlank()) {
            role = "STUDENT"; // default role
        }

        if (fullName.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
            model.addAttribute("error", "All fields are required!");
            return "auth/register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "auth/register";
        }

        try {
            registrationService.register(username, password, fullName, email, role);
            model.addAttribute("success", "Account created successfully! You can login now.");
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
