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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/teacher/exams")
public class ExamTeacherController {

    private final ExamService examService;
    private final ClassesService classesService;
    private final StudentService studentService;

    public ExamTeacherController(ExamService examService,
                                 ClassesService classesService,
                                 StudentService studentService) {
        this.examService = examService;
        this.classesService = classesService;
        this.studentService = studentService;
    }

    // ================= LIST EXAMS =================
    @GetMapping
    public String listExams(Model model) {
        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("classes", classesService.getAllClasses());
        model.addAttribute("newExam", new Exam());
        return "teacher/exams";
    }

    // ================= SAVE EXAM =================
    @PostMapping("/save")
    public String saveExam(@ModelAttribute("newExam") Exam exam,
                           @RequestParam("classId") Long classId) {

        Classes clazz = classesService.getClassById(classId);
        if (clazz == null) {
            return "redirect:/teacher/exams?error=classNotFound";
        }

        exam.setClasses(clazz);

        if (exam.getExamDate() == null) {
            exam.setExamDate(LocalDate.now().plusDays(7));
        }

        examService.saveExam(exam);
        return "redirect:/teacher/exams";
    }

    // ================= UPLOAD RESULTS FORM =================
    @GetMapping("/results/{examId}")
    public String uploadResultsForm(@PathVariable Long examId, Model model) {

        Exam exam = examService.getExamById(examId).orElse(null); // ✅ unwrap Optional
        if (exam == null) {
            return "redirect:/teacher/exams?error=examNotFound";
        }

        List<Student> students = studentService.getStudentsByClasses(exam.getClasses());

        model.addAttribute("exam", exam);
        model.addAttribute("students", students);
        return "teacher/exam-results-upload";
    }

    // ================= SAVE RESULTS =================
    @PostMapping("/results/save/{examId}")
    public String saveResults(@PathVariable Long examId,
                              @RequestParam List<Long> studentIds,
                              @RequestParam List<Double> marks,
                              @RequestParam List<String> grades,
                              @RequestParam List<String> remarks) {

        Exam exam = examService.getExamById(examId).orElse(null); // ✅ unwrap Optional
        if (exam == null) {
            return "redirect:/teacher/exams?error=examNotFound";
        }

        for (int i = 0; i < studentIds.size(); i++) {
            Student student = studentService.getById(studentIds.get(i)).orElse(null); // ✅ unwrap Optional
            if (student != null) {
                Exam.ExamResult result = new Exam.ExamResult();
                result.setStudent(student);
                result.setMarks(marks.get(i));
                result.setGrade(grades.get(i));
                result.setRemarks(remarks.get(i));

                examService.addOrUpdateResult(examId, result);
            }
        }

        return "redirect:/teacher/exams/results/" + examId + "?success=resultsSaved";
    }

    // ================= DELETE EXAM =================
    @GetMapping("/delete/{id}")
    public String deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return "redirect:/teacher/exams";
    }
}