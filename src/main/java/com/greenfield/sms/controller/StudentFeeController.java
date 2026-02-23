package com.greenfield.sms.controller;

import com.greenfield.sms.model.Fee;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.repository.FeeRepository;
import com.greenfield.sms.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentFeeController {

    private final StudentRepository studentRepository;
    private final FeeRepository feeRepository;

    @Autowired
    public StudentFeeController(StudentRepository studentRepository,
                                FeeRepository feeRepository) {
        this.studentRepository = studentRepository;
        this.feeRepository = feeRepository;
    }

    // ================= VIEW FEES =================
    @GetMapping("/student/fees")
    public String viewFees(Model model, Principal principal) {

        // Redirect to login if user not authenticated
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();

        // Fetch student linked to the current logged-in user
        Optional<Student> optionalStudent = studentRepository.findByUserUsername(username);

        if (optionalStudent.isEmpty()) {
            model.addAttribute("errorMessage", "No student record linked to this account.");
            return "error/student-not-found";
        }

        Student student = optionalStudent.get();

        // Fetch all fees for this student ordered by due date descending
        List<Fee> fees = feeRepository.findByStudentOrderByDueDateDesc(student);

        // Add student, fees, and className to the model
        model.addAttribute("student", student);
        model.addAttribute("fees", fees);
        model.addAttribute("className", student.getUser().getClassName()); // <-- NEW

        return "student/fees";
    }
}
