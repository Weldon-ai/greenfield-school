package com.greenfield.sms.controller;

import com.greenfield.sms.model.Teacher;
import com.greenfield.sms.model.TeacherStatus;
import com.greenfield.sms.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final TeacherService teacherService;

    public AdminTeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // ===== LIST TEACHERS =====
    @GetMapping
    public String viewAllTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        model.addAttribute("totalTeachers", teachers.size());
        model.addAttribute("statuses", TeacherStatus.values());
        model.addAttribute("newTeacher", new Teacher());
        return "admin/teachers";
    }

    // ===== ADD / SAVE NEW TEACHER =====
    @PostMapping("/new")
    public String saveNewTeacher(@ModelAttribute("newTeacher") Teacher teacher,
                                 @RequestParam String password) {

        // ===== Validate mandatory fields =====
        if (teacher.getFirstName() == null || teacher.getFirstName().isBlank()
                || teacher.getLastName() == null || teacher.getLastName().isBlank()
                || teacher.getEmail() == null || teacher.getEmail().isBlank()
                || teacher.getUsername() == null || teacher.getUsername().isBlank()
                || teacher.getSubject() == null || teacher.getSubject().isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException("All fields including password are mandatory.");
        }

        // ===== Set defaults =====
        teacher.setStatus(TeacherStatus.ACTIVE);
        teacher.setEnabled(true);
        teacher.setDateJoined(LocalDate.now());

        // ===== Save teacher & linked user (with ROLE_TEACHER) =====
        teacherService.saveTeacher(teacher, password);

        return "redirect:/admin/teachers";
    }

    // ===== EDIT TEACHER =====
    @PostMapping("/edit/{id}")
    public String updateTeacher(@PathVariable Long id,
                                @ModelAttribute Teacher teacher,
                                @RequestParam(required = false) String password) {

        // Retrieve existing teacher
        Teacher existing = teacherService.getTeacherById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + id));

        // Update teacher fields
        existing.setFirstName(teacher.getFirstName());
        existing.setLastName(teacher.getLastName());
        existing.setUsername(teacher.getUsername());
        existing.setEmail(teacher.getEmail());
        existing.setPhone(teacher.getPhone());
        existing.setGender(teacher.getGender());
        existing.setSubject(teacher.getSubject());

        // ===== Update teacher & linked user =====
        teacherService.updateTeacher(existing, password);

        return "redirect:/admin/teachers";
    }

    // ===== DELETE TEACHER =====
    @PostMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/admin/teachers";
    }

    // ===== CHANGE TEACHER STATUS =====
    @PostMapping("/status/{id}")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam TeacherStatus status) {
        switch (status) {
            case ACTIVE -> teacherService.enableTeacher(id);
            case DISABLED -> teacherService.disableTeacher(id);
            case SUSPENDED -> teacherService.suspendTeacher(id);
            case RETIRED -> teacherService.retireTeacher(id);
            case TERMINATED -> teacherService.terminateTeacher(id);
        }
        return "redirect:/admin/teachers";
    }
}
