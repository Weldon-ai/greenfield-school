package com.greenfield.sms.model; // Package for model enums

/**
 * Represents the lifecycle state of an assignment
 * Used by both teacher and student sides
 */
public enum AssignmentStatus {

    DRAFT,      // Assignment created but not yet visible to students

    PUBLISHED,  // Assignment is visible and can be submitted

    CLOSED      // Assignment is no longer accepting submissions
}
