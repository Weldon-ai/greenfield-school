package com.greenfield.sms.controller;

import com.greenfield.sms.model.Attendance;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.User;
import com.greenfield.sms.repository.StudentRepository;
import com.greenfield.sms.repository.UserRepository;
import com.greenfield.sms.service.AttendanceService;
import com.greenfield.sms.service.ClassesService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ClassesService classesService;

    public AttendanceController(
            AttendanceService attendanceService,
            UserRepository userRepository,
            StudentRepository studentRepository,
            ClassesService classesService
    ) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.classesService = classesService;
    }

    @GetMapping("/attendance")
    public String viewAttendance(Model model, Authentication authentication) {

        // 1️⃣ Get the logged-in user
        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (userOpt.isEmpty()) {
            model.addAttribute("message", "User account not found.");
            return "error/student-error";
        }
        User user = userOpt.get();

        // 2️⃣ Get the student profile linked to the user
        Optional<Student> studentOpt = studentRepository.findByUser(user);
        if (studentOpt.isEmpty()) {
            model.addAttribute("message",
                    "Your student profile has not yet been created. Please contact the school administration.");
            return "error/student-error";
        }
        Student student = studentOpt.get();

        // 3️⃣ Get the current class of the student (use className)
        Classes currentClass = student.getUser().getClasses();

        // 4️⃣ Fetch attendance records for this student
        List<Attendance> attendanceRecords = attendanceService.getAttendanceForStudent(student);

        // 5️⃣ Prepare attendance percentage map for chart rendering
        Map<String, Integer> attendancePercentage = attendanceRecords.stream()
                .collect(Collectors.groupingBy(
                        att -> att.getStudent().getFullName(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    long presentCount = list.stream().filter(Attendance::isPresent).count();
                                    return list.isEmpty() ? 0 : (int) ((presentCount * 100) / list.size());
                                }
                        )
                ));

        // 6️⃣ Fetch all classes for potential filters in the UI
        List<Classes> allClasses = classesService.getAllClasses();

        // 7️⃣ Send data to Thymeleaf template
        model.addAttribute("studentName", student.getFullName());             // Student name
        model.addAttribute("currentClassName", currentClass != null ? currentClass.getName() : "N/A"); // NEW
        model.addAttribute("attendanceList", attendanceRecords);              // Attendance records
        model.addAttribute("attendancePercentage", attendancePercentage);     // Chart data
        model.addAttribute("classes", allClasses);                            // All classes for filter
        model.addAttribute("selectedClassName", currentClass != null ? currentClass.getName() : null); // NEW
        model.addAttribute("startDate", null); // Placeholder for future filter
        model.addAttribute("endDate", null);   // Placeholder for future filter

        return "student/attendance";
    }

    @GetMapping("/test")
    public String testTemplate() {
        return "students/test";
    }
}
