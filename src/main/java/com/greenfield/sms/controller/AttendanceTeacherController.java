package com.greenfield.sms.controller;

import com.greenfield.sms.model.Attendance;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.RecordedBy;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.service.AttendanceService;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/attendance")
public class AttendanceTeacherController {

    private final ClassesService classesService;
    private final StudentService studentService;
    private final AttendanceService attendanceService;

    public AttendanceTeacherController(ClassesService classesService,
                                       StudentService studentService,
                                       AttendanceService attendanceService) {
        this.classesService = classesService;
        this.studentService = studentService;
        this.attendanceService = attendanceService;
    }

    // =========================
    // Overview of Classes
    // =========================
    @GetMapping
    public String attendanceOverview(Model model) {
        model.addAttribute("allClasses", classesService.getAllClasses());
        return "teacher/attendance-overview";
    }

    // =========================
    // Take / Edit Attendance Form
    // =========================
    @GetMapping("/take/{classId}")
    public String takeAttendanceForm(@PathVariable Long classId,
                                     @RequestParam(required = false) String date,
                                     Model model) {

        // ✅ Use getClassById() safely
        Classes classes = classesService.getClassById(classId);
        if (classes == null) {
            throw new IllegalArgumentException("Invalid class ID: " + classId);
        }

        LocalDate attendanceDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();

        List<Student> students = studentService.getStudentsByClasses(classes);

        List<Attendance> existing = students.stream()
                .map(student -> attendanceService.getAttendanceForStudentAndDate(student, classes, attendanceDate))
                .filter(a -> a != null)
                .collect(Collectors.toList());

        Set<Long> presentStudentIds = existing.stream()
                .filter(Attendance::isPresent)
                .map(a -> a.getStudent().getId())
                .collect(Collectors.toSet());

        model.addAttribute("classes", classes);
        model.addAttribute("students", students);
        model.addAttribute("date", attendanceDate);
        model.addAttribute("presentStudentIds", presentStudentIds);

        return "teacher/attendance-take";
    }

    // =========================
    // Save Attendance
    // =========================
    @PostMapping("/save")
    public String saveAttendance(@RequestParam Long classId,
                                 @RequestParam String date,
                                 @RequestParam(required = false) List<Long> presentStudentIds) {

        Classes classes = classesService.getClassById(classId);
        if (classes == null) {
            throw new IllegalArgumentException("Invalid class ID: " + classId);
        }

        LocalDate attendanceDate = LocalDate.parse(date);

        List<Student> students = studentService.getStudentsByClasses(classes);

        for (Student student : students) {
            boolean isPresent = presentStudentIds != null && presentStudentIds.contains(student.getId());

            attendanceService.recordAttendance(
                    student,
                    classes,
                    attendanceDate,
                    isPresent,
                    RecordedBy.TEACHER,
                    null
            );
        }

        return "redirect:/teacher/attendance?success=saved";
    }
}