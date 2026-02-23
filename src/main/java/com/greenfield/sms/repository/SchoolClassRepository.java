package com.greenfield.sms.repository;

import com.greenfield.sms.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    // No extra methods needed for now
    // Spring Data JPA provides findAll(), findById(), save(), deleteById() automatically
}
