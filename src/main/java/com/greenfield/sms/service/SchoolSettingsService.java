package com.greenfield.sms.service;

import com.greenfield.sms.model.SchoolSettings;
import com.greenfield.sms.repository.SchoolSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchoolSettingsService {

    private final SchoolSettingsRepository repository;

    public SchoolSettingsService(SchoolSettingsRepository repository) {
        this.repository = repository;
    }

    public SchoolSettings getSettings() {
        Optional<SchoolSettings> settings = repository.findAll().stream().findFirst();
        return settings.orElse(new SchoolSettings());
    }

    public SchoolSettings saveSettings(SchoolSettings settings) {
        return repository.save(settings);
    }
}
