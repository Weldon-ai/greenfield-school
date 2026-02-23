package com.greenfield.sms.controller;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/classes")
public class AdminClassController {

    private final ClassesService classesService;
    private final StudentService studentService;

    public AdminClassController(ClassesService classesService, StudentService studentService) {
        this.classesService = classesService;
        this.studentService = studentService;
    }

    // ================= VIEW ALL CLASSES =================
    @GetMapping
    public String viewAllClasses(Model model) {
        List<Classes> classes = classesService.getAllClassesOrdered();
        model.addAttribute("classes", classes);
        model.addAttribute("totalClasses", classes.size());
        return "admin/classes";
    }

    // ================= VIEW STUDENTS IN A CLASS =================
    @GetMapping("/{id}/students")
    public String viewStudentsInClass(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        // FIX: Unwrap Optional using .orElse(null)
        Classes schoolClass = classesService.getById(id).orElse(null);
        
        if (schoolClass == null) {
            redirectAttributes.addFlashAttribute("error", "Class record not found.");
            return "redirect:/admin/classes";
        }

        Set<Student> students = schoolClass.getStudents();
        model.addAttribute("schoolClass", schoolClass);
        model.addAttribute("students", students);
        model.addAttribute("totalStudents", (students != null) ? students.size() : 0);
        return "admin/class-students";
    }

    // ================= STUDENT ACTIONS =================

    @PostMapping("/{classId}/students/{studentId}/promote")
    public String promoteStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        Classes schoolClass = classesService.getById(classId).orElse(null);
        // Assuming studentService.getById also returns Optional
        Student student = studentService.getById(studentId).orElse(null);
        
        if (schoolClass != null && student != null) {
            classesService.promoteStudents(schoolClass); 
        }
        return "redirect:/admin/classes/" + classId + "/students";
    }

    @PostMapping("/{classId}/students/{studentId}/defer")
    public String deferStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        Classes schoolClass = classesService.getById(classId).orElse(null);
        Student student = studentService.getById(studentId).orElse(null);
        
        if (schoolClass != null && student != null) {
            classesService.deferStudent(student, schoolClass);
        }
        return "redirect:/admin/classes/" + classId + "/students";
    }

    @PostMapping("/{classId}/students/{studentId}/retain")
    public String retainStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        Classes schoolClass = classesService.getById(classId).orElse(null);
        Student student = studentService.getById(studentId).orElse(null);
        
        if (schoolClass != null && student != null) {
            classesService.retainStudent(student, schoolClass);
        }
        return "redirect:/admin/classes/" + classId + "/students";
    }

    @PostMapping("/{classId}/students/{studentId}/remove")
    public String removeStudent(@PathVariable Long classId, @PathVariable Long studentId) {
        Classes schoolClass = classesService.getById(classId).orElse(null);
        Student student = studentService.getById(studentId).orElse(null);
        
        if (schoolClass != null && student != null) {
            classesService.removeStudentFromClass(student, schoolClass);
        }
        return "redirect:/admin/classes/" + classId + "/students";
    }

    // ================= ADD NEW CLASS =================
    @GetMapping("/add")
    public String addClassForm(Model model) {
        model.addAttribute("newClass", new Classes());
        return "admin/add-class";
    }

    @PostMapping("/add")
    public String saveNewClass(@ModelAttribute("newClass") Classes newClass, RedirectAttributes redirectAttributes) {
        try {
            classesService.saveClass(newClass);
            redirectAttributes.addFlashAttribute("success", "Class created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating class: " + e.getMessage());
        }
        return "redirect:/admin/classes";
    }
}