package com.greenfield.sms.service;

import com.greenfield.sms.model.Assignment;
import com.greenfield.sms.model.Student;
import com.greenfield.sms.model.Submission;
import com.greenfield.sms.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    /**
     * Submit or update an assignment with PDF support
     *
     * @param student    the submitting student
     * @param assignment the assignment to submit
     * @param content    optional text content/comments
     * @param pdfPath    the filename/path of the uploaded PDF
     * @return the saved Submission
     */
    public Submission submitAssignment(Student student,
                                       Assignment assignment,
                                       String content,
                                       String pdfPath) {

        // Check if student already submitted this assignment
        Optional<Submission> existing =
                submissionRepository.findByStudentAndAssignment(student, assignment);

        Submission submission = existing.orElse(new Submission());

        if (existing.isEmpty()) {
            submission.setStudent(student);
            submission.setAssignment(assignment);
        }

        // Update fields for both new and existing submissions
        submission.setContent(content);
        submission.setPdfPath(pdfPath); // Save the new PDF filename
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    /**
     * Fetch all submissions by a given student
     */
    public List<Submission> getSubmissionsByStudent(Student student) {
        return submissionRepository.findByStudent(student);
    }

    /**
     * Fetch all submissions for a given assignment (used by teachers)
     */
    public List<Submission> getSubmissionsByAssignment(Assignment assignment) {
        return submissionRepository.findByAssignment(assignment);
    }

    /**
     * Find a specific submission by ID
     */
    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id).orElse(null);
    }

    /**
     * Grade a submission with marks and feedback
     *
     * @param submissionId the ID of the submission
     * @param marks        marks awarded
     * @param feedback     teacher's comments
     * @return the updated submission
     */
    public Submission gradeSubmission(Long submissionId, Integer marks, String feedback) {
        Submission submission = getSubmissionById(submissionId);
        if (submission != null) {
            submission.setMarks(marks);
            submission.setFeedback(feedback); // Record why they got that grade
            return submissionRepository.save(submission);
        }
        return null;
    }
}