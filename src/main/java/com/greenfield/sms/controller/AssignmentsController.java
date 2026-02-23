package com.greenfield.sms.controller;

import com.greenfield.sms.model.Assignment;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.User;
import com.greenfield.sms.service.AssignmentService;
import com.greenfield.sms.service.SubmissionService;
import com.greenfield.sms.service.FileStorageService;
import com.greenfield.sms.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/student")
public class AssignmentsController {

    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public AssignmentsController(AssignmentService assignmentService,
                                 SubmissionService submissionService,
                                 UserRepository userRepository,
                                 FileStorageService fileStorageService) {
        this.assignmentService = assignmentService;
        this.submissionService = submissionService;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/assignments")
    public String assignments(Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Get Student directly from User
        Student student = user.getStudent(); 
        if (student == null) {
            throw new RuntimeException("Student profile not found");
        }

        Classes studentClass = student.getClasses();
        List<Assignment> assignments;
        String className;

        if (studentClass != null) {
            assignments = assignmentService.getAssignmentsForClass(studentClass);
            className = studentClass.getName();
        } else {
            assignments = List.of();
            className = "Not assigned";
        }

        model.addAttribute("assignments", assignments);
        model.addAttribute("className", className);
        model.addAttribute("studentName", student.getFullName());

        return "student/assignments";
    }

    @GetMapping("/assignments/submit")
    public String submitForm(@RequestParam("id") Long id, Model model) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        if (assignment == null) return "redirect:/student/assignments";

        model.addAttribute("assignment", assignment);
        return "student/submit-assignment";
    }

    @PostMapping("/assignments/submit")
    public String submitAssignment(@RequestParam("id") Long id,
                                   @RequestParam(value = "content", required = false) String content,
                                   @RequestParam("pdfFile") MultipartFile pdfFile,
                                   Authentication authentication,
                                   RedirectAttributes ra) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Student student = user.getStudent();
        if (student == null) {
            ra.addFlashAttribute("error", "Student profile not found");
            return "redirect:/student/assignments";
        }

        Assignment assignment = assignmentService.getAssignmentById(id);
        if (assignment == null) {
            ra.addFlashAttribute("error", "Assignment not found");
            return "redirect:/student/assignments";
        }

        try {
            String fileName = null;

            if (pdfFile != null && !pdfFile.isEmpty()) {
                if (!"application/pdf".equals(pdfFile.getContentType())) {
                    ra.addFlashAttribute("error", "Invalid file type. Please upload a PDF.");
                    return "redirect:/student/assignments/submit?id=" + id;
                }
                fileName = fileStorageService.saveFile(pdfFile);
            }

            submissionService.submitAssignment(student, assignment, content, fileName);
            ra.addFlashAttribute("success", "Assignment submitted successfully!");

        } catch (IOException e) {
            ra.addFlashAttribute("error", "File upload error: " + e.getMessage());
            return "redirect:/student/assignments/submit?id=" + id;
        }

        return "redirect:/student/assignments";
    }
}