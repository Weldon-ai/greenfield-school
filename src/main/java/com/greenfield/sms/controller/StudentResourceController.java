package com.greenfield.sms.controller;

import com.greenfield.sms.model.Resource;
import com.greenfield.sms.repository.ResourceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentResourceController {

    private final ResourceRepository resourceRepository;

    public StudentResourceController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/student/resources")
    public String viewResources(Model model) {

        // Get all resources sorted by uploaded date
        List<Resource> resources = resourceRepository.findAllByOrderByUploadedAtDesc();
        model.addAttribute("resources", resources);

        return "student/resources"; // maps to templates/students/resources.html
    }
}
