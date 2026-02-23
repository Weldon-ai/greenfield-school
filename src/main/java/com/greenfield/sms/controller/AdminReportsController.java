package com.greenfield.sms.controller;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.Subject;
import com.greenfield.sms.model.Teacher;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.StudentService;
import com.greenfield.sms.service.SubjectService;
import com.greenfield.sms.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminReportsController {

    private final StudentService studentService;
    private final ClassesService classesService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;

    public AdminReportsController(StudentService studentService,
                                  ClassesService classesService,
                                  TeacherService teacherService,
                                  SubjectService subjectService) {
        this.studentService = studentService;
        this.classesService = classesService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    @GetMapping("/admin/reports")
    public String reportsPage(Model model,
                              @RequestParam(required = false) Long classId,
                              @RequestParam(required = false) Long teacherId,
                              @RequestParam(required = false) Long subjectId) {

        // ------------------- Fetch all filters -------------------
        List<Classes> classes = classesService.getAllClassesOrdered();
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Subject> subjects = subjectService.getAllSubjects();

        // ------------------- Fetch students -------------------
        List<Student> students = studentService.getAllStudents();

        // Apply filters if present
        if (classId != null) {
            students = students.stream()
                    .filter(s -> s.getCurrentClass() != null && s.getCurrentClass().getId().equals(classId))
                    .collect(Collectors.toList());
        }

        if (teacherId != null) {
            students = students.stream()
                    .filter(s -> s.getAssignedTeacher() != null && s.getAssignedTeacher().getId().equals(teacherId))
                    .collect(Collectors.toList());
        }

        if (subjectId != null) {
            students = students.stream()
                    .filter(s -> s.getSubjects() != null && s.getSubjects().stream()
                            .anyMatch(sub -> sub.getId().equals(subjectId)))
                    .collect(Collectors.toList());
        }

        // ------------------- Add attributes to model -------------------
        model.addAttribute("classes", classes);
        model.addAttribute("teachers", teachers);
        model.addAttribute("subjects", subjects);
        model.addAttribute("students", students);

        return "admin/reports";
    }
}
