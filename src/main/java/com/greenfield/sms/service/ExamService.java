package com.greenfield.sms.service;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Exam;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    // ===================== EXISTING METHODS =====================

    // Get all exams
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // Get exams by class
    public List<Exam> getExamsByClass(Classes classes) {
        return examRepository.findByClasses(classes);
    }

    // Save or update an exam
    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    // Get exam by ID
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    // Delete exam
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    // ===================== TEACHER-SIDE RESULT MANAGEMENT =====================

    public Exam.ExamResult addOrUpdateResult(Long examId, Exam.ExamResult result) {
        Optional<Exam> examOpt = examRepository.findById(examId);
        if (examOpt.isPresent()) {
            Exam exam = examOpt.get();
            exam.addOrUpdateResult(result); // from Exam model
            examRepository.save(exam);
            return result;
        }
        return null;
    }

    public void removeResult(Long examId, Exam.ExamResult result) {
        Optional<Exam> examOpt = examRepository.findById(examId);
        if (examOpt.isPresent()) {
            Exam exam = examOpt.get();
            exam.removeResult(result);
            examRepository.save(exam);
        }
    }

    // ===================== NEW ADVANCED METHODS =====================

    // Lock/unlock exams
    public void lockExam(Long examId) {
        getExamById(examId).ifPresent(exam -> {
            exam.setLocked(true);
            examRepository.save(exam);
        });
    }

    public void unlockExam(Long examId) {
        getExamById(examId).ifPresent(exam -> {
            exam.setLocked(false);
            examRepository.save(exam);
        });
    }

    // Get exams by term/period
    public List<Exam> getExamsByPeriod(String term) {
        return examRepository.findByTerm(term);
    }

    // Get all results for a student across exams
    public List<Exam.ExamResult> getResultsForStudent(Student student) {
        return examRepository.findResultsByStudent(student);
    }

    // Get all results for a class for a specific exam
    public List<Exam.ExamResult> getResultsForClass(Long examId) {
        return getExamById(examId)
                .map(Exam::getResults)
                .orElse(Collections.emptyList());
    }

    // Analytics: average, highest, lowest marks
    public Map<String, Double> getExamAnalytics(Long examId) {
        Map<String, Double> analytics = new HashMap<>();
        getExamById(examId).ifPresent(exam -> {
            analytics.put("average", exam.getAverageMark());
            analytics.put("highest", exam.getHighestMark());
            analytics.put("lowest", exam.getLowestMark());
        });
        return analytics;
    }

    // Count passed/failed students
    public long countPassedStudents(Long examId, double passingMark) {
        return getExamById(examId)
                .map(exam -> exam.getResults().stream()
                        .filter(r -> r.getMarks() != null && r.getMarks() >= passingMark)
                        .count())
                .orElse(0L);
    }

    public long countFailedStudents(Long examId, double passingMark) {
        return getExamById(examId)
                .map(exam -> exam.getResults().stream()
                        .filter(r -> r.getMarks() != null && r.getMarks() < passingMark)
                        .count())
                .orElse(0L);
    }

    // Fetch results for export (per exam or class)
    public List<Exam.ExamResult> getResultsForExport(Long examId) {
        return examRepository.findResultsByExamId(examId);
    }

    // ===================== ADVANCED FEATURES =====================

    // Fetch top N students per exam
    public List<Exam.ExamResult> getTopResults(Long examId, int topN) {
        return getResultsForClass(examId).stream()
                .sorted((r1, r2) -> Double.compare(r2.getMarks(), r1.getMarks()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    // Fetch exams within date range for a class
    public List<Exam> getExamsByClassAndDateRange(Classes classes, LocalDate startDate, LocalDate endDate) {
        return examRepository.findByClassAndDateRange(classes, startDate, endDate);
    }

    // Fetch exams that are open (not locked) for a class
    public List<Exam> getOpenExamsByClass(Classes classes) {
        return examRepository.findOpenExamsByClass(classes);
    }

    // Fetch exams with number of results (analytics for admin dashboard)
    public Map<Exam, Integer> getExamsWithResultCount(Classes classes) {
        Map<Exam, Integer> resultCountMap = new HashMap<>();
        examRepository.findExamsWithResultCount(classes).forEach(obj -> {
            Exam exam = (Exam) obj[0];
            Integer count = (Integer) obj[1];
            resultCountMap.put(exam, count);
        });
        return resultCountMap;
    }
}
