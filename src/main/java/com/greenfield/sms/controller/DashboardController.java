
package com.greenfield.sms.controller;

import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.StudentService;
import com.greenfield.sms.service.TeacherService;
import com.greenfield.sms.service.WeatherService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {

    private final WeatherService weatherService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ClassesService classesService;

    public DashboardController(WeatherService weatherService, 
                               StudentService studentService, 
                               TeacherService teacherService, 
                               ClassesService classesService) {
        this.weatherService = weatherService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.classesService = classesService;
    }

    // ================= ADMIN DASHBOARD =================
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("role", "Admin");

        Map<String, Object> stats = new HashMap<>();
        stats.put("studentCount", studentService.countStudents());
        stats.put("teacherCount", teacherService.countTeachers());
        stats.put("classCount", classesService.countClasses());
        stats.put("monthlyRevenue", "KSh 0.00"); // placeholder
        stats.put("pendingFees", "KSh 0.00");    // placeholder

        model.addAttribute("dashboardStats", stats);
        addCommonDashboardData(model);

        return "admin/admin-dashboard"; // Thymeleaf template
    }

    // ================= TEACHER DASHBOARD =================
    @GetMapping("/teacher/dashboard")
    public String teacherDashboard(Model model) {
        model.addAttribute("role", "Teacher");
        addCommonDashboardData(model);
        return "teacher/teacher-dashboard";
    }

    // ================= PARENT DASHBOARD =================
    @GetMapping("/parent/dashboard")
    public String parentDashboard(Model model) {
        model.addAttribute("role", "Parent");
        addCommonDashboardData(model);
        return "parent/parent-dashboard";
    }

    // ================= FINANCE DASHBOARD =================
    @GetMapping("/finance/dashboard")
    public String financeDashboard(Model model) {
        model.addAttribute("role", "Finance");
        addCommonDashboardData(model);
        return "finance/finance-dashboard";
    }

    // ================= AUTHENTICATED ENTRY POINT =================
    @GetMapping("/dashboard")
    public String dashboardRedirect(Authentication authentication) {
        // Not logged in → go to public home page
        if (authentication == null 
                || !authentication.isAuthenticated() 
                || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/home";
        }

        // Logged-in users → redirect based on role
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            switch (role) {
                case "ROLE_ADMIN": return "redirect:/admin/dashboard";
                case "ROLE_TEACHER": return "redirect:/teacher/dashboard";
                case "ROLE_STUDENT": return "redirect:/student/dashboard";
                case "ROLE_PARENT": return "redirect:/parent/dashboard";
                case "ROLE_FINANCE": return "redirect:/finance/dashboard";
            }
        }

        // Default fallback
        return "redirect:/home";
    }

    // ================= HELPER METHODS =================
    private void addCommonDashboardData(Model model) {
        try {
            Map<String, Object> weather = weatherService.getWeather("Eldoret");
            model.addAttribute("weather", weather);
        } catch (Exception e) {
            model.addAttribute("weatherError", "Weather currently unavailable");
        }
    }
}
