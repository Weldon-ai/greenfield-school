package com.greenfield.sms.dto;

public class DashboardStats {
    private long studentCount;
    private long teacherCount;
    private long classCount;
    private String monthlyRevenue;
    private String pendingFees;

    // Constructors, Getters, and Setters
    public DashboardStats(long students, long teachers, long classes, String revenue, String fees) {
        this.studentCount = students;
        this.teacherCount = teachers;
        this.classCount = classes;
        this.monthlyRevenue = revenue;
        this.pendingFees = fees;
    }
    
    // Getters must match the HTML: getStudentCount(), getTeacherCount(), etc.
    public long getStudentCount() { return studentCount; }
    public long getTeacherCount() { return teacherCount; }
    public long getClassCount() { return classCount; }
    public String getMonthlyRevenue() { return monthlyRevenue; }
    public String getPendingFees() { return pendingFees; }
}