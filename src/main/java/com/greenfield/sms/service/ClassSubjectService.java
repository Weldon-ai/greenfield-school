package com.greenfield.sms.service; // Service package

import com.greenfield.sms.model.ClassSubject; // Mapping entity
import com.greenfield.sms.repository.ClassSubjectRepository; // Repository
import org.springframework.stereotype.Service; // Service annotation

@Service // Marks service
public class ClassSubjectService {

    private final ClassSubjectRepository repository; // Repository

    public ClassSubjectService(ClassSubjectRepository repository) { // Constructor
        this.repository = repository; // Assign repository
    }

    public ClassSubject save(ClassSubject cs) { // Save mapping
        return repository.save(cs); // Persist
    }
}
