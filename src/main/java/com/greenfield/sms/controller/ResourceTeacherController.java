package com.greenfield.sms.controller;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.Resource;
import com.greenfield.sms.service.ClassesService;
import com.greenfield.sms.service.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/teacher/resources")
public class ResourceTeacherController {

    private final ResourceService resourceService;
    private final ClassesService classesService;

    public ResourceTeacherController(ResourceService resourceService,
                                     ClassesService classesService) {
        this.resourceService = resourceService;
        this.classesService = classesService;
    }

    // ================= LIST ALL RESOURCES =================
    @GetMapping
    public String listResources(Model model,
                                @ModelAttribute("success") String success,
                                @ModelAttribute("error") String error) {

        List<Resource> resources = resourceService.getAllResources();
        List<Classes> classes = classesService.getAllClasses();

        model.addAttribute("resources", resources != null ? resources : List.of());
        model.addAttribute("classes", classes != null ? classes : List.of());
        model.addAttribute("newResource", new Resource());
        model.addAttribute("success", success != null ? success : "");
        model.addAttribute("error", error != null ? error : "");

        return "teacher/resources"; // Thymeleaf template path
    }

    // ================= SAVE NEW RESOURCE =================
    @PostMapping("/save")
    public String saveResource(@ModelAttribute("newResource") Resource resource,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               RedirectAttributes redirectAttributes) {

        try {
            // ===== Validate class selection =====
            if (resource.getClasses() == null || resource.getClasses().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Please select a class.");
                return "redirect:/teacher/resources";
            }

            Classes clazz = classesService.getClassById(resource.getClasses().getId());
            if (clazz == null) {
                redirectAttributes.addFlashAttribute("error", "Selected class not found.");
                return "redirect:/teacher/resources";
            }

            // ===== Set associations =====
            resource.setClasses(clazz);
            resource.setUploadedBy(null); // uploadedBy can be null
            resource.setUploadedAt(LocalDateTime.now());

            // ===== Save file if provided =====
            if (file != null && !file.isEmpty()) {
                resource.setFilePath(saveFile(file));
            } else {
                resource.setFilePath(null); // no file uploaded
            }

            // ===== Persist resource =====
            resourceService.saveResource(resource);
            redirectAttributes.addFlashAttribute("success", "Resource uploaded successfully!");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "File upload failed: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving resource: " + e.getMessage());
        }

        return "redirect:/teacher/resources";
    }

    // ================= DELETE RESOURCE =================
    @GetMapping("/delete/{id}")
    public String deleteResource(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            resourceService.deleteResource(id);
            redirectAttributes.addFlashAttribute("success", "Resource deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not delete resource: " + e.getMessage());
        }
        return "redirect:/teacher/resources";
    }

    // ================= FILE SAVE HELPER =================
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "src/main/resources/static/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(dir.getAbsolutePath() + File.separator + fileName);
        file.transferTo(dest);

        return "uploads/" + fileName; // relative path for Thymeleaf
    }
}