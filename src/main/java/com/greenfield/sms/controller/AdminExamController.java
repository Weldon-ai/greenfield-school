package com.greenfield.sms.controller;

import com.greenfield.sms.model.Exam;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.service.ExamService;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/exams")
public class AdminExamController {

    private final ExamService examService;
    private final ClassesService classesService;
    private final StudentService studentService;

    public AdminExamController(ExamService examService,
                               ClassesService classesService,
                               StudentService studentService) {
        this.examService = examService;
        this.classesService = classesService;
        this.studentService = studentService;
    }

    // ================= DASHBOARD =================
    @GetMapping
    public String examDashboard(Model model) {
        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("allClasses", classesService.getAllClasses());
        return "admin/exam-dashboard";
    }

    // ================= CREATE EXAM =================
    @GetMapping("/create")
    public String createExamForm(Model model) {
        model.addAttribute("exam", new Exam());
        model.addAttribute("allClasses", classesService.getAllClasses());
        return "admin/exam-form";
    }

    // ================= SAVE EXAM =================
@PostMapping("/save")
public String saveExam(@ModelAttribute Exam exam,
                       @RequestParam("classId") Long classId) {

    // Classes is returned directly
    Classes clazz = classesService.getClassById(classId);
    if (clazz == null) {
        return "redirect:/admin/exams?error=classNotFound";
    }

    exam.setClasses(clazz);
    examService.saveExam(exam);
    return "redirect:/admin/exams";
}

    // ================= UPLOAD RESULTS =================
    @GetMapping("/results/{examId}")
    public String uploadResults(@PathVariable Long examId, Model model) {
        Exam exam = examService.getExamById(examId).orElse(null);
        if (exam == null) {
            return "redirect:/admin/exams?error=examNotFound";
        }

        List<Student> students = studentService.getStudentsByClasses(exam.getClasses());

        List<Double> existingMarks = new ArrayList<>();
        List<String> existingGrades = new ArrayList<>();
        List<String> existingRemarks = new ArrayList<>();

        for (Student student : students) {
            Exam.ExamResult result = exam.getResults().stream()
                    .filter(r -> r.getStudent().getId().equals(student.getId()))
                    .findFirst()
                    .orElse(new Exam.ExamResult());
            existingMarks.add(result.getMarks());
            existingGrades.add(result.getGrade());
            existingRemarks.add(result.getRemarks());
        }

        model.addAttribute("exam", exam);
        model.addAttribute("students", students);
        model.addAttribute("existingMarks", existingMarks);
        model.addAttribute("existingGrades", existingGrades);
        model.addAttribute("existingRemarks", existingRemarks);

        return "admin/exam-results-upload";
    }

    // ================= SAVE RESULTS =================
    @PostMapping("/results/save/{examId}")
    public String saveResults(@PathVariable Long examId,
                              @RequestParam(value = "studentIds", required = false) List<Long> studentIds,
                              @RequestParam(value = "marks", required = false) List<Double> marks,
                              @RequestParam(value = "grades", required = false) List<String> grades,
                              @RequestParam(value = "remarks", required = false) List<String> remarks) {

        Exam exam = examService.getExamById(examId).orElse(null);
        if (exam == null) {
            return "redirect:/admin/exams?error=examNotFound";
        }

        if (studentIds == null || studentIds.isEmpty()) {
            return "redirect:/admin/exams/results/" + examId + "?error=noStudentsSelected";
        }

        if (marks == null || grades == null || remarks == null ||
                studentIds.size() != marks.size() || marks.size() != grades.size() || grades.size() != remarks.size()) {
            return "redirect:/admin/exams/results/" + examId + "?error=invalidInput";
        }

        for (int i = 0; i < studentIds.size(); i++) {
            final int index = i; // lambda-safe
            Student student = studentService.getById(studentIds.get(index)).orElse(null);
            if (student == null) continue; // skip missing student

            Exam.ExamResult result = new Exam.ExamResult();
            result.setStudent(student);
            result.setMarks(marks.get(index));
            result.setGrade(grades.get(index));
            result.setRemarks(remarks.get(index));

            examService.addOrUpdateResult(examId, result);
        }

        return "redirect:/admin/exams/results/" + examId + "?success=resultsSaved";
    }

    // ================= LOCK / UNLOCK =================
    @PostMapping("/lock/{examId}")
    public String lockExam(@PathVariable Long examId) {
        Exam exam = examService.getExamById(examId).orElse(null);
        if (exam != null) {
            exam.setLocked(true);
            examService.saveExam(exam);
        }
        return "redirect:/admin/exams";
    }

    @PostMapping("/unlock/{examId}")
    public String unlockExam(@PathVariable Long examId) {
        Exam exam = examService.getExamById(examId).orElse(null);
        if (exam != null) {
            exam.setLocked(false);
            examService.saveExam(exam);
        }
        return "redirect:/admin/exams";
    }

    // ================= EXPORT =================
    @GetMapping("/export/excel/{examId}")
    public StreamingResponseBody exportExamExcel(@PathVariable Long examId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // TODO: Implement Excel export logic
        return outputStream::writeTo;
    }

    @GetMapping("/export/pdf/{examId}")
    public StreamingResponseBody exportExamPdf(@PathVariable Long examId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // TODO: Implement PDF export logic
        return outputStream::writeTo;
    }
}