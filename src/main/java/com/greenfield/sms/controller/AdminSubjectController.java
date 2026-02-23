package com.greenfield.sms.controller;

import com.greenfield.sms.model.Subject;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Teacher;
import com.greenfield.sms.service.SubjectService;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.TeacherService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/subjects")
public class AdminSubjectController {

    private final SubjectService subjectService;
    private final ClassesService classesService;
    private final TeacherService teacherService;

    public AdminSubjectController(SubjectService subjectService,
                                  ClassesService classesService,
                                  TeacherService teacherService) {
        this.subjectService = subjectService;
        this.classesService = classesService;
        this.teacherService = teacherService;
    }

    // ================= LIST ALL SUBJECTS =================
    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("allClasses", classesService.getAllClasses());
        return "admin/subjects";
    }

    // ================= SHOW CREATE SUBJECT FORM =================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "admin/add-subject";
    }

    // ================= CREATE NEW SUBJECT =================
    @PostMapping("/new")
    public String createSubject(@ModelAttribute("subject") Subject subject,
                                RedirectAttributes redirectAttributes) {
        try {
            subjectService.saveSubject(subject);
            redirectAttributes.addFlashAttribute("success", "Subject created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not create subject: " + e.getMessage());
        }
        return "redirect:/admin/subjects";
    }

    // ================= UPDATE SUBJECT =================
    @PostMapping("/save")
    public String saveSubject(@ModelAttribute("subject") Subject subject,
                              RedirectAttributes redirectAttributes) {
        try {
            subjectService.saveSubject(subject);
            redirectAttributes.addFlashAttribute("success", "Subject updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating subject: " + e.getMessage());
        }
        return "redirect:/admin/subjects";
    }

    // ================= DELETE SUBJECT =================
    @PostMapping("/delete/{id}")
    public String deleteSubject(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            subjectService.deleteSubject(id);
            redirectAttributes.addFlashAttribute("success", "Subject deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete subject: " + e.getMessage());
        }
        return "redirect:/admin/subjects";
    }

    // ================= SHOW ASSIGN CLASSES FORM =================
    @GetMapping("/assign-class/{id}")
    public String showAssignClassForm(@PathVariable Long id,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        Optional<Subject> subjectOpt = subjectService.getById(id);

        if (subjectOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Subject not found!");
            return "redirect:/admin/subjects";
        }

        model.addAttribute("subject", subjectOpt.get());
        model.addAttribute("allClasses", classesService.getAllClasses());
        return "admin/assign-class";
    }

    // ================= ASSIGN SUBJECT TO MULTIPLE CLASSES =================
    @PostMapping("/assign-class/{id}")
    public String assignSubjectToClasses(@PathVariable Long id,
                                         @RequestParam(value = "classIds", required = false) List<Long> classIds,
                                         RedirectAttributes redirectAttributes) {
        try {
            // Use the correct service method
            subjectService.updateSubjectClasses(id, classIds);
            redirectAttributes.addFlashAttribute("success", "Subject curriculum updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update curriculum: " + e.getMessage());
        }
        return "redirect:/admin/subjects/assign-class/" + id;
    }

    // ================= REMOVE SUBJECT FROM CLASS =================
    @PostMapping("/remove-class")
    public String removeSubjectFromClass(@RequestParam Long subjectId,
                                         @RequestParam Long classId,
                                         RedirectAttributes redirectAttributes) {
        try {
            subjectService.removeClass(subjectId, classId);
            redirectAttributes.addFlashAttribute("success", "Subject removed from class successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error removing subject: " + e.getMessage());
        }
        return "redirect:/admin/subjects";
    }

    // ================= SHOW ASSIGN TEACHER FORM =================
    @GetMapping("/assign-teacher/{id}")
    public String showAssignTeacherForm(@PathVariable Long id,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        Optional<Subject> subjectOpt = subjectService.getById(id);

        if (subjectOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Subject not found!");
            return "redirect:/admin/subjects";
        }

        model.addAttribute("subject", subjectOpt.get());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "admin/assign-teacher";
    }

    // ================= ASSIGN TEACHER TO SUBJECT =================
    @PostMapping("/assign-teacher")
    public String assignTeacher(@RequestParam Long subjectId,
                                @RequestParam Long teacherId,
                                RedirectAttributes redirectAttributes) {
        try {
            subjectService.assignTeacher(subjectId, teacherId);
            redirectAttributes.addFlashAttribute("success", "Teacher assigned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error assigning teacher: " + e.getMessage());
        }
        return "redirect:/admin/subjects";
    }
}