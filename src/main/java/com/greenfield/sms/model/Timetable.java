package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "timetables")
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
private String studentClass;
    private String subject;

    private LocalTime startTime;
    private LocalTime endTime;

    private String room;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    // Each timetable entry is linked to a class
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    // ===== Constructors =====
    public Timetable() {}

    public Timetable(String subject, LocalTime startTime, LocalTime endTime, String room, DayOfWeek dayOfWeek, SchoolClass schoolClass) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.schoolClass = schoolClass;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
public String getStudentClass(){return studentClass;}
public void setStudentClass(String studentClass){this.studentClass= studentClass;}
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }
}
