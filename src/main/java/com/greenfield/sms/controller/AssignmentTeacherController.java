package com.greenfield.sms.controller;

import com.greenfield.sms.model.Assignment;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Submission;
import com.greenfield.sms.service.AssignmentService;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.SubmissionService; // Added
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/teacher/assignments")
public class AssignmentTeacherController {

    private final AssignmentService assignmentService;
    private final ClassesService classesService;
    private final SubmissionService submissionService; // Added

    public AssignmentTeacherController(AssignmentService assignmentService,
                                       ClassesService classesService,
                                       SubmissionService submissionService) {
        this.assignmentService = assignmentService;
        this.classesService = classesService;
        this.submissionService = submissionService;
    }

    /**
     * View all assignments
     */
    @GetMapping
    public String listAssignments(Model model) {
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        
        Assignment newAssignment = new Assignment();
        newAssignment.setClasses(new Classes()); 
        
        model.addAttribute("assignment", newAssignment);
        model.addAttribute("classes", classesService.getAllClasses());
        return "teacher/assignments";
    }

    /**
     * ✅ NEW: Analytics Endpoint (Fixes the "No static resource" error)
     */
    @GetMapping("/analytics")
    public String showAnalytics(Model model) {
        List<Assignment> allAssignments = assignmentService.getAllAssignments();
        model.addAttribute("totalAssignments", allAssignments.size());
        // Add more logic here to calculate averages/rates for your charts
        return "teacher/analytics";
    }

    /**
     * Edit assignment
     */
    @GetMapping("/edit")
    public String editAssignment(@RequestParam("id") Long id, Model model) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        if (assignment == null) return "redirect:/teacher/assignments";

        model.addAttribute("assignment", assignment);
        model.addAttribute("classes", classesService.getAllClasses());
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        return "teacher/assignments";
    }

    /**
     * Save/Update assignment
     */
    @PostMapping("/save")
    public String saveAssignment(@ModelAttribute("assignment") Assignment assignment) {
        if (assignment.getDueDate() == null) {
            assignment.setDueDate(LocalDate.now().plusDays(7));
        }
        assignmentService.saveAssignment(assignment);
        return "redirect:/teacher/assignments";
    }

    /**
     * Delete assignment
     */
    @GetMapping("/delete")
    public String deleteAssignment(@RequestParam("id") Long id) {
        assignmentService.deleteAssignment(id);
        return "redirect:/teacher/assignments";
    }

    /**
     * ✅ UPDATED: Show Grade form with actual Submissions
     */
    @GetMapping("/grade")
    public String gradeAssignment(@RequestParam("id") Long id, Model model) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        if (assignment == null) return "redirect:/teacher/assignments";

        // Fetch the actual work submitted by students for this specific assignment
        List<Submission> submissions = submissionService.getSubmissionsByAssignment(assignment);
        
        model.addAttribute("assignment", assignment);
        model.addAttribute("submissions", submissions); // This prevents the 500 error in your template
        
        return "teacher/assignment-grade";
    }

    /**
     * ✅ UPDATED: Submit grade for a specific Student Submission
     */
    @PostMapping("/grade/submit")
    public String submitGrade(@RequestParam("submissionId") Long submissionId,
                              @RequestParam("marks") Integer marks,
                              @RequestParam("feedback") String feedback) {

        // Use the submission service to save the grade against the specific student's work
        submissionService.gradeSubmission(submissionId, marks, feedback);
        
        // Find assignment ID to redirect back to the correct grading page
        Submission sub = submissionService.getSubmissionById(submissionId);
        return "redirect:/teacher/assignments/grade?id=" + sub.getAssignment().getId();
    }
}