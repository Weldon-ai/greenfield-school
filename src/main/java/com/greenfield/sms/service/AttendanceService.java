package com.greenfield.sms.service;

import com.greenfield.sms.model.Attendance;
import com.greenfield.sms.model.RecordedBy;
import com.greenfield.sms.model.Classes; // Updated
import com.greenfield.sms.model.Student;
import com.greenfield.sms.repository.AttendanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // ================= STUDENT / TEACHER METHODS =================

    /** Get all attendance records for a student */
    public List<Attendance> getAttendanceForStudent(Student student) {
        if (student == null) return Collections.emptyList();
        return attendanceRepository.findByStudent(student);
    }

    /** Save or update a single attendance record */
    public Attendance saveAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    /** Get attendance for a student for a specific class and date */
    public Attendance getAttendanceForStudentAndDate(Student student, Classes classes, LocalDate date) {
        if (student == null || classes == null || date == null) return null;
        // Repository must match this naming convention
        return attendanceRepository.findByStudentAndClassesAndDate(student, classes, date).orElse(null);
    }

    // ================= ADMIN / CLASS METHODS =================

    /** Fetch all attendance records */
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    /** * Updated: Matches the compilation error fix 
     */
    public List<Attendance> getAttendanceForClass(Classes classes) {
        if (classes == null) return Collections.emptyList();
        return attendanceRepository.findByClasses(classes);
    }

    /** * Updated: Get attendance for a class filtered by date range 
     */
    public List<Attendance> getAttendanceForClassAndDateRange(Classes classes, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return Collections.emptyList();
        if (classes != null) {
            return attendanceRepository.findByClassesAndDateBetween(classes, startDate, endDate);
        } else {
            return attendanceRepository.findByDateBetween(startDate, endDate);
        }
    }

    /** Fetch attendance by ID */
    public Attendance getById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    // ================= ATTENDANCE PERCENTAGE =================

    /** Calculate attendance percentage for a student */
    public double calculateAttendancePercentage(Student student) {
        List<Attendance> records = getAttendanceForStudent(student);
        if (records.isEmpty()) return 0.0;

        long presentCount = records.stream().filter(Attendance::isPresent).count();
        return (presentCount * 100.0) / records.size();
    }

    /** Calculate attendance percentage for all students in a class */
    public Map<Student, Double> attendancePercentagePerClass(Classes classes) {
        List<Attendance> records = getAttendanceForClass(classes);
        if (records.isEmpty()) return Collections.emptyMap();

        return records.stream()
                .collect(Collectors.groupingBy(
                        Attendance::getStudent,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream().filter(Attendance::isPresent).count() * 100.0 / list.size()
                        )
                ));
    }

    // ================= AUTO-LOCK JOB =================

    /** Automatically locks attendance older than X days */
    @Transactional
    @Scheduled(cron = "0 0 1 * * ?") // Daily at 1 AM
    public void autoLockAttendance() {
        int lockAfterDays = 3;
        List<Attendance> unlockedRecords = attendanceRepository.findByLockedFalse();

        unlockedRecords.forEach(att -> {
            long daysOld = ChronoUnit.DAYS.between(att.getDate(), LocalDate.now());
            if (daysOld > lockAfterDays) {
                att.lock();
                attendanceRepository.save(att);
            }
        });
    }

    // ================= UTILITY METHODS =================

    /** Check if an attendance record is editable */
    public boolean isEditable(Attendance attendance, int lockAfterDays) {
        if (attendance == null || attendance.isLocked()) return false;
        long daysPassed = ChronoUnit.DAYS.between(attendance.getDate(), LocalDate.now());
        return daysPassed <= lockAfterDays;
    }

    /** Record attendance (create or update) */
    public Attendance recordAttendance(Student student, Classes classes, LocalDate date,
                                       boolean present, RecordedBy recordedBy, Long userId) {
        Attendance existing = getAttendanceForStudentAndDate(student, classes, date);
        if (existing != null) {
            existing.setPresent(present);
            existing.setRecordedBy(recordedBy);
            existing.setRecordedByUserId(userId);
            return saveAttendance(existing);
        } else {
            Attendance attendance = new Attendance(student, classes, date, present, recordedBy);
            attendance.setRecordedByUserId(userId);
            return saveAttendance(attendance);
        }
    }
}