package com.greenfield.sms.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ClassLevel {
    PLAY_GROUP("Play Group"),
    PP1("PP1"),
    PP2("PP2"),
    GRADE_1("Grade 1"),
    GRADE_2("Grade 2"),
    GRADE_3("Grade 3"),
    GRADE_4("Grade 4"),
    GRADE_5("Grade 5"),
    GRADE_6("Grade 6"),
    GRADE_7("Grade 7"),
    GRADE_8("Grade 8"),
    GRADE_9("Grade 9");

    private final String displayName;

    // Constructor
    ClassLevel(String displayName) {
        this.displayName = displayName;
    }

    // For Thymeleaf or JSON output
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
