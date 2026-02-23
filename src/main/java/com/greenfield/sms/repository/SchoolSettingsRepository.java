package com.greenfield.sms.repository;

import com.greenfield.sms.model.SchoolSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolSettingsRepository extends JpaRepository<SchoolSettings, Long> {
}
