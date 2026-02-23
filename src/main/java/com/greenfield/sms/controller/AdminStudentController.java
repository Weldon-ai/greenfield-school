package com.greenfield.sms.controller;

import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.StudentStatus;
import com.greenfield.sms.model.User;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.StudentService;
import com.greenfield.sms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {

    private final StudentService studentService;
    private final ClassesService classesService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminStudentController(StudentService studentService,
                                  ClassesService classesService,
                                  UserService userService,
                                  PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.classesService = classesService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ======================================================
    // VIEW STUDENTS WITH OPTIONAL FILTERS
    // ======================================================
    @GetMapping
    public String viewAllStudents(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) StudentStatus status,
            Model model) {

        List<Student> students;

        if (classId != null && status != null) {
            students = studentService.getStudentsByClassAndStatus(classId, status);
        } else if (classId != null) {
            students = studentService.getStudentsByClass(classId);
        } else if (status != null) {
            students = studentService.getByStatus(status);
        } else {
            students = studentService.getAllStudents();
        }

        model.addAttribute("students", students);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("allClasses", classesService.getAllClasses());
        model.addAttribute("selectedClassId", classId);
        model.addAttribute("selectedStatus", status);

        return "admin/students";
    }

    // ======================================================
    // SHOW ADD STUDENT FORM
    // ======================================================
    @GetMapping("/new")
    public String showAddStudentForm(Model model) {
        if (!model.containsAttribute("student")) {
            Student student = new Student();
            student.setUser(new User());
            model.addAttribute("student", student);
        }
        model.addAttribute("allClasses", classesService.getAllClasses());
        return "admin/new";
    }

    // ======================================================
    // SAVE NEW STUDENT
    // ======================================================
    @PostMapping("/save")
    public String addStudent(@ModelAttribute("student") Student student,
                             @RequestParam(required = false) Long classId,
                             RedirectAttributes redirectAttributes) {
        try {
            // ===== SETUP USER ACCOUNT =====
            User user = student.getUser();
            if (user != null) {
                String rawPassword = (user.getPassword() != null && !user.getPassword().isBlank()) 
                        ? user.getPassword() 
                        : "student123";
                user.setPassword(passwordEncoder.encode(rawPassword));
                student.setUser(userService.addStudentUser(user));
            }

            // ===== SET DEFAULT STUDENT FIELDS =====
            student.setStatus(StudentStatus.ACTIVE);
            student.setEnabled(true);

            // ===== LINK CLASS IF PROVIDED =====
            if (classId != null) {
                student.setClasses(classesService.getClassById(classId));
            }

            studentService.addNewStudent(student);

            redirectAttributes.addFlashAttribute("success", "Student saved successfully!");
            return "redirect:/admin/students";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save student: " + e.getMessage());
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/admin/students/new";
        }
    }

    // ======================================================
    // DELETE STUDENT
    // ======================================================
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "Student deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete student: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }

    // ======================================================
    // ENABLE / DISABLE STUDENT
    // ======================================================
    @PostMapping("/enable/{id}")
    public String enableStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.updateStatus(id, StudentStatus.ACTIVE);
        redirectAttributes.addFlashAttribute("success", "Student enabled successfully.");
        return "redirect:/admin/students";
    }

    @PostMapping("/disable/{id}")
    public String disableStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.updateStatus(id, StudentStatus.DISABLED);
        redirectAttributes.addFlashAttribute("success", "Student disabled successfully.");
        return "redirect:/admin/students";
    }

    // ======================================================
    // PROMOTE STUDENT
    // ======================================================
    @PostMapping("/promote/{id}")
    public String promoteStudent(@PathVariable Long id,
                                 @RequestParam Long nextClassId,
                                 RedirectAttributes redirectAttributes) {
        try {
            studentService.promoteStudent(id, nextClassId);
            redirectAttributes.addFlashAttribute("success", "Student promoted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Promotion failed: " + e.getMessage());
        }
        return "redirect:/admin/students?classId=" + nextClassId;
    }

    // ======================================================
    // ASSIGN STUDENT TO CLASS
    // ======================================================
    @PostMapping("/assign-class/{id}")
    public String assignClass(@PathVariable Long id,
                              @RequestParam Long classId,
                              RedirectAttributes redirectAttributes) {
        try {
            studentService.assignToClass(id, classId);
            redirectAttributes.addFlashAttribute("success", "Class assigned successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Assigning class failed: " + e.getMessage());
        }
        return "redirect:/admin/students?classId=" + classId;
    }
}
