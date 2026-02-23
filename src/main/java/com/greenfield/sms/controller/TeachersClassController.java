package com.greenfield.sms.controller;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.SchemeOfWork;
import com.greenfield.sms.model.Teacher;
import com.greenfield.sms.service.SchemeOfWorkService;
import com.greenfield.sms.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList; // Added for safety
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher") 
public class TeachersClassController {

    private final TeacherService teacherService;
    private final SchemeOfWorkService schemeService;

    // Standard constructor-based injection
    public TeachersClassController(TeacherService teacherService, SchemeOfWorkService schemeService) {
        this.teacherService = teacherService;
        this.schemeService = schemeService;
    }

    // ================= SCHEMES OF WORK =================

    @GetMapping("/schemes")
    public String listMySchemes(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        
        String fullName = getUserFullName(principal.getName());
        List<SchemeOfWork> allSchemes = schemeService.getAllSchemes();
        
        List<SchemeOfWork> mySchemes = (allSchemes != null) ? allSchemes.stream()
                .filter(s -> s.getTeacherName() != null && s.getTeacherName().equalsIgnoreCase(fullName))
                .collect(Collectors.toList()) : new ArrayList<>();
        
        model.addAttribute("schemes", mySchemes);
        return "teacher/schemes-list";
    }

    @GetMapping("/schemes/new")
    public String showSchemeForm(Model model, Principal principal) {
        SchemeOfWork newScheme = new SchemeOfWork();
        if (principal != null) {
            teacherService.getByEmail(principal.getName()).ifPresent(t -> {
                newScheme.setTeacherName(t.getFullName());
            });
        }
        model.addAttribute("scheme", newScheme);
        return "teacher/create-scheme";
    }

    @PostMapping("/schemes/save")
    public String saveScheme(@ModelAttribute("scheme") SchemeOfWork scheme, RedirectAttributes ra) {
        if (scheme.getId() == null) {
            scheme.setStatus("PENDING");
        }
        schemeService.saveScheme(scheme);
        ra.addFlashAttribute("success", "Scheme saved successfully!");
        return "redirect:/teacher/schemes";
    }

    // ================= LEARNING OUTCOMES =================

    @GetMapping("/outcomes")
    public String viewLearningOutcomes(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        
        String fullName = getUserFullName(principal.getName());
        List<SchemeOfWork> allSchemes = schemeService.getAllSchemes();

        List<SchemeOfWork> mySchemes = (allSchemes != null) ? allSchemes.stream()
                .filter(s -> s.getTeacherName() != null && s.getTeacherName().equalsIgnoreCase(fullName))
                .collect(Collectors.toList()) : new ArrayList<>();
        
        model.addAttribute("schemes", mySchemes);
        return "teacher/outcomes"; 
    }

    // ================= CLASSES & LESSONS =================

    @GetMapping("/classes")
    public String myClasses(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        
        Teacher teacher = teacherService.getByEmail(principal.getName()).orElse(null);
        model.addAttribute("teacher", teacher);
        model.addAttribute("classes", (teacher != null) ? teacher.getClasses() : new ArrayList<>());
        return "teacher/classes";
    }

    @GetMapping("/lessons")
    public String viewMyLessons(Model model, Principal principal) {
        if (principal != null) {
            teacherService.getByEmail(principal.getName()).ifPresent(t -> {
                model.addAttribute("teacher", t);
                model.addAttribute("classes", t.getClasses());
            });
        }
        return "teacher/lessons";
    }

    // ================= DIAGNOSTIC ROUTE (DEBUGGING) =================
    // If you visit /teacher/debug and see "OK", the controller IS working.
    @GetMapping("/debug")
    @ResponseBody
    public String debugController() {
        return "Teacher Controller is active and registered!";
    }

    // ================= HELPER METHODS =================

    private String getUserFullName(String email) {
        return teacherService.getByEmail(email)
                .map(Teacher::getFullName)
                .orElse("Unknown Teacher");
    }
}