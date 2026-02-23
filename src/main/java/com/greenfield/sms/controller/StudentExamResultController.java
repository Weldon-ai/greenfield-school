package com.greenfield.sms.controller;

import com.greenfield.sms.model.*;
import com.greenfield.sms.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class StudentExamResultController {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;

    public StudentExamResultController(
            StudentRepository studentRepository,
            UserRepository userRepository,
            ExamRepository examRepository,
            ResultRepository resultRepository
    ) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.examRepository = examRepository;
        this.resultRepository = resultRepository;
    }

    @GetMapping("/student/exams")
    public String examsAndResults(Model model, Principal principal) {
        // 1. Safety Check: Ensure user is logged in
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            // 2. Fetch User & Student Profile
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User session invalid"));

            Student student = studentRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Student profile missing"));

            // 3. Handle Class Assignment (Single object, not collection)
            Classes studentClass = user.getClasses();
            List<Exam> exams = Collections.emptyList();
            String className = "Not Assigned";

            if (studentClass != null) {
                exams = examRepository.findByClasses(studentClass);
                className = studentClass.getClassName();
            }

            // 4. Fetch Results & Statistics
            List<Result> results = resultRepository.findByStudent(student);
            double gpa = calculateGPA(results);

            // 5. Populate the Model for Thymeleaf
            model.addAttribute("studentName", user.getFullName());
            model.addAttribute("className", className);
            model.addAttribute("exams", exams);
            model.addAttribute("results", results);
            model.addAttribute("gpa", String.format("%.2f", gpa));

        } catch (Exception e) {
            // Log the error and show a user-friendly message
            model.addAttribute("error", "Could not load academic data: " + e.getMessage());
            return "student/exams"; 
        }

        return "student/exams"; // Path: src/main/resources/templates/student/exams.html
    }

    private double calculateGPA(List<Result> results) {
        if (results == null || results.isEmpty()) return 0.0;
        double totalPoints = 0;
        for (Result res : results) {
            double marks = res.getMarksObtained();
            if (marks >= 90) totalPoints += 4.0;
            else if (marks >= 80) totalPoints += 3.5;
            else if (marks >= 70) totalPoints += 3.0;
            else if (marks >= 60) totalPoints += 2.5;
            else if (marks >= 50) totalPoints += 2.0;
            else totalPoints += 1.0;
        }
        return totalPoints / results.size();
    }
}