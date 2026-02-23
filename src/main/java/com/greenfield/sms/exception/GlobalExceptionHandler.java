package com.greenfield.sms.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(MultipartException e, RedirectAttributes redirectAttributes) {
        // This catches the FileCountLimitExceededException and MaxUploadSizeExceededException
        redirectAttributes.addFlashAttribute("errorMessage", "Too many files or file size too large!");
        return "redirect:/admin/settings-dashboard";
    }
}