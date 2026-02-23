package com.greenfield.sms.controller;

import com.greenfield.sms.model.SchoolSettings;
import com.greenfield.sms.service.SchoolSettingsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class SchoolSettingsController {

    private final SchoolSettingsService service;

    public SchoolSettingsController(SchoolSettingsService service) {
        this.service = service;
    }

    /**
     * Display School Settings dashboard
     */
    @GetMapping("/settings-dashboard")
    public String showSettingsDashboard(Model model) {
        SchoolSettings settings = service.getSettings();
        if (settings == null) {
            settings = new SchoolSettings();
        }
        model.addAttribute("schoolSettings", settings);
        return "admin/settings-dashboard";
    }

    /**
     * Save or update School Settings
     */
    @PostMapping("/settings/save")
    public String saveSettings(
            @Valid @ModelAttribute("schoolSettings") SchoolSettings schoolSettings,
            BindingResult bindingResult,
            @RequestParam(value = "logoUpload", required = false) MultipartFile logoUpload,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {

        // 1. Validation errors
        if (bindingResult.hasErrors()) {
            // Note: Returning the view directly instead of redirecting 
            // allows you to show specific field validation errors.
            return "admin/settings-dashboard";
        }

        // 2. Fetch existing settings to preserve the logo if no new one is uploaded
        SchoolSettings existingSettings = service.getSettings();

        // 3. Handle logo upload safely
        if (logoUpload != null && !logoUpload.isEmpty()) {
            // Check size (5MB)
            if (logoUpload.getSize() > 5 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("errorMessage", "Logo must not exceed 5MB");
                return "redirect:/admin/settings-dashboard";
            }

            try {
                // Map to your model fields: logoFile (byte[]), logoFileType, logoFileName
                schoolSettings.setLogoFile(logoUpload.getBytes());
                schoolSettings.setLogoFileType(logoUpload.getContentType());
                schoolSettings.setLogoFileName(logoUpload.getOriginalFilename());
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to process logo file");
                return "redirect:/admin/settings-dashboard";
            }
        } else if (existingSettings != null) {
            // If no new file uploaded, keep the old one so it doesn't become null
            schoolSettings.setLogoFile(existingSettings.getLogoFile());
            schoolSettings.setLogoFileType(existingSettings.getLogoFileType());
            schoolSettings.setLogoFileName(existingSettings.getLogoFileName());
        }

        // 4. Audit fields
        String username = (principal != null) ? principal.getName() : "admin";
        schoolSettings.setLastModifiedBy(username);
        schoolSettings.setLastModifiedAt(LocalDateTime.now());

        // 5. Save settings
        service.saveSettings(schoolSettings);

        redirectAttributes.addFlashAttribute("successMessage", "School settings saved successfully");
        return "redirect:/admin/settings-dashboard";
    }
}