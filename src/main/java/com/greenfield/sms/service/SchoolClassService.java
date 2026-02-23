// SchoolClassService.java
package com.greenfield.sms.service;

import com.greenfield.sms.model.SchoolClass;
import com.greenfield.sms.repository.SchoolClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolClassService {

    private final SchoolClassRepository classRepository;

    public SchoolClassService(SchoolClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    // Get all classes
    public List<SchoolClass> getAllClasses() {
        return classRepository.findAll();
    }

    // Get class by ID safely as Optional
    public Optional<SchoolClass> getClassById(Long id) {
        return classRepository.findById(id);
    }
}
