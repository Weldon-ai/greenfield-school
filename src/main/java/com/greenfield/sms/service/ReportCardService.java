package com.greenfield.sms.service;

import com.greenfield.sms.model.Classes;
import com.greenfield.sms.model.ReportCard;
import com.greenfield.sms.repository.ReportCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportCardService {

    @Autowired
    private ReportCardRepository reportCardRepository;

    /**
     * Fixes: The method findAllReports() is undefined
     */
    public List<ReportCard> findAllReports() {
        return reportCardRepository.findAll();
    }

    /**
     * Finds reports for a specific class
     */
    public List<ReportCard> findByClasses(Classes classes) {
        return reportCardRepository.findByClasses(classes);
    }

    /**
     * Deletes all reports for a specific class
     */
    public void deleteByClass(Classes classes) {
        List<ReportCard> reports = reportCardRepository.findByClasses(classes);
        if (!reports.isEmpty()) {
            reportCardRepository.deleteAll(reports);
        }
    }

    /**
     * Find a single report by ID
     */
    public ReportCard findById(Long id) {
        return reportCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report Card not found with id: " + id));
    }
}