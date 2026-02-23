package com.greenfield.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("/student")
public class StudentClassesController {

    @GetMapping("student/classes")
    public String classes() {
        return "student/classes";
    }
    
}