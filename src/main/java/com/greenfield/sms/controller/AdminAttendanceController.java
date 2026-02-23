package com.greenfield.sms.controller;

import com.greenfield.sms.model.Attendance;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.service.AttendanceService;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/attendance")
public class AdminAttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final ClassesService classesService;

    // ================= CONSTRUCTOR INJECTION =================
    @Autowired
    public AdminAttendanceController(AttendanceService attendanceService,
                                     StudentService studentService,
                                     ClassesService classesService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.classesService = classesService;
    }

    // ================= DASHBOARD VIEW =================
    @GetMapping
    public String attendanceDashboard(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<Classes> allClasses = classesService.getAllClasses();
        model.addAttribute("allClasses", allClasses);

        List<Attendance> attendanceList = fetchFilteredAttendance(classId, startDate, endDate);

        Map<Long, Double> attendancePercentage = attendanceList.stream()
                .collect(Collectors.groupingBy(
                        att -> att.getStudent().getId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    long total = list.size();
                                    long presentCount = list.stream().filter(Attendance::isPresent).count();
                                    return total > 0 ? (presentCount * 100.0 / total) : 0.0;
                                }
                        )
                ));

        Map<Long, Student> studentMap = attendanceList.stream()
                .map(Attendance::getStudent)
                .distinct()
                .collect(Collectors.toMap(
                        Student::getId,
                        student -> student
                ));

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("attendancePercentage", attendancePercentage);
        model.addAttribute("studentMap", studentMap);
        model.addAttribute("selectedClassId", classId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/attendance-dashboard";
    }

    // ================= UPDATE ATTENDANCE =================
    @PostMapping("/update/{id}")
    public String updateAttendance(@PathVariable Long id,
                                   @RequestParam boolean present) {

        Attendance attendance = attendanceService.getById(id);

        if (attendance != null && !attendance.isLocked()) {
            attendance.setPresent(present);
            attendanceService.saveAttendance(attendance);
        }

        return "redirect:/admin/attendance";
    }

    // ================= EXPORT STUBS =================
    @GetMapping("/export/pdf")
    public StreamingResponseBody exportPdf(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return outputStreamStub();
    }

    @GetMapping("/export/excel")
    public StreamingResponseBody exportExcel(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return outputStreamStub();
    }

    // ================= HELPER METHOD =================
    private List<Attendance> fetchFilteredAttendance(Long classId, LocalDate startDate, LocalDate endDate) {

        if (classId != null) {
            Classes selectedClass = classesService.getClassById(classId);
            if (selectedClass == null) return List.of();

            if (startDate != null && endDate != null) {
                return attendanceService.getAttendanceForClassAndDateRange(selectedClass, startDate, endDate);
            }
            return attendanceService.getAttendanceForClass(selectedClass);
        }

        if (startDate != null && endDate != null) {
            return attendanceService.getAllAttendance().stream()
                    .filter(att -> !att.getDate().isBefore(startDate) && !att.getDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        return attendanceService.getAllAttendance();
    }

    private StreamingResponseBody outputStreamStub() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return outputStream::writeTo;
    }
}