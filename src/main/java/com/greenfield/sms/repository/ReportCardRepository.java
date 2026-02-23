package com.greenfield.sms.repository;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.ReportCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportCardRepository extends JpaRepository<ReportCard, Long> {
    List<ReportCard> findByClasses(Classes classes);
    List<ReportCard> findAll();
}