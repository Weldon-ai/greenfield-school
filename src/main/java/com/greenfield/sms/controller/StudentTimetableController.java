package com.greenfield.sms.controller;

import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.Timetable;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.repository.StudentRepository;
import com.greenfield.sms.service.TimetableService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class StudentTimetableController {

    private final StudentRepository studentRepository;
    private final TimetableService timetableService;

    public StudentTimetableController(StudentRepository studentRepository, TimetableService timetableService) {
        this.studentRepository = studentRepository;
        this.timetableService = timetableService;
    }

    @GetMapping("/student/timetable")
    public String viewTimetable(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        Student student = studentRepository.findByUserUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // FIX: Check if the single Classes object is null instead of calling .isEmpty()
        Classes studentClass = student.getClasses(); 

        if (studentClass == null) {
            model.addAttribute("timetable", Collections.emptyList()); 
            model.addAttribute("message", "You are not currently enrolled in a class.");
        } else {
            List<Timetable> timetable = timetableService.getTimetableForClass(studentClass);
            
            // Feature: Sort timetable by day and time for a better UX
            timetable.sort(Comparator.comparing(Timetable::getDayOfWeek)
                                     .thenComparing(Timetable::getStartTime));
            
            model.addAttribute("timetable", timetable);
            model.addAttribute("className", studentClass.getClassName());
        }

        return "student/timetable";
    }
}