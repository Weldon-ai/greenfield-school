package com.greenfield.sms.service;

import com.greenfield.sms.model.Attendance;
import com.greenfield.sms.repository.AttendanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceLockScheduler {

    private final AttendanceRepository attendanceRepository;

    /** Lock attendance records after X days */
    private final int LOCK_AFTER_DAYS = 3; // example: lock after 3 days

    public AttendanceLockScheduler(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    /**
     * Run every day at 00:00 to auto-lock old attendance records
     */
    @Scheduled(cron = "0 0 0 * * ?") // midnight every day
    @Transactional
    public void autoLockAttendance() {
        LocalDate today = LocalDate.now();
        List<Attendance> recordsToLock = attendanceRepository.findByLockedFalseAndDateBefore(today.minusDays(LOCK_AFTER_DAYS));

        for (Attendance attendance : recordsToLock) {
            attendance.lock(); // mark as locked and set lockedAt
            attendanceRepository.save(attendance);
        }

        System.out.println("Auto-locked " + recordsToLock.size() + " attendance records.");
    }
}
