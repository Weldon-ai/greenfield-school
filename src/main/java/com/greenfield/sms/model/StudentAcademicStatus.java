package com.greenfield.sms.model;

public enum StudentAcademicStatus {
    ACTIVE,        // Normal learning
    PROMOTED,      // Moved to next class
    RETAINED,      // Repeats same class
    DEFERRED,      // Temporarily paused
    REMOVED        // Expelled / transferred
}
