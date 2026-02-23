package com.greenfield.sms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadPath = "uploads/assignments/";

    public String saveFile(MultipartFile file) throws IOException {
        // 1. Create directory if it doesn't exist
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        // 2. Generate unique filename to prevent overwriting
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        // 3. Save the file
        Files.copy(file.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }
}