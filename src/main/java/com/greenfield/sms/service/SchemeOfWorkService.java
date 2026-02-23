package com.greenfield.sms.service;

import com.greenfield.sms.model.SchemeOfWork;
import com.greenfield.sms.repository.SchemeOfWorkRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SchemeOfWorkService {

    private final SchemeOfWorkRepository repository;

    public SchemeOfWorkService(SchemeOfWorkRepository repository) {
        this.repository = repository;
    }

    public SchemeOfWork saveScheme(SchemeOfWork scheme) {
        return repository.save(scheme);
    }

    public SchemeOfWork getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scheme not found with ID: " + id));
    }

    public List<SchemeOfWork> getAllSchemes() {
        return repository.findAll();
    }
}