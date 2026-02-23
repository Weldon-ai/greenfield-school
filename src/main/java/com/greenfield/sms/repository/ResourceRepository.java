package com.greenfield.sms.repository;

import com.greenfield.sms.model.Resource;
import com.greenfield.sms.model.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for Resource entities.
 * Provides methods for fetching resources with optional eager fetching of classes.
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    /**
     * Fetch all resources sorted by uploadedAt descending.
     * @return List of resources
     */
    List<Resource> findAllByOrderByUploadedAtDesc();

    /**
     * Fetch all resources assigned to a specific class, sorted by uploadedAt descending.
     * @param classes the class entity
     * @return List of resources
     */
    List<Resource> findByClassesOrderByUploadedAtDesc(Classes classes);

    /**
     * Fetch resources for a class by its ID, sorted by uploadedAt descending.
     * @param classId ID of the class
     * @return List of resources
     */
    List<Resource> findByClassesIdOrderByUploadedAtDesc(Long classId);

    /**
     * Fetch all resources with their associated classes eagerly loaded.
     * Useful to avoid lazy-loading issues when accessing r.classes in Thymeleaf templates.
     * @return List of resources with classes loaded
     */
    @Query("SELECT r FROM Resource r LEFT JOIN FETCH r.classes ORDER BY r.uploadedAt DESC")
    List<Resource> findAllWithClasses();

    /**
     * Fetch resources for a specific class ID with classes eagerly loaded.
     * @param classId ID of the class
     * @return List of resources for the class
     */
    @Query("SELECT r FROM Resource r LEFT JOIN FETCH r.classes c WHERE c.id = :classId ORDER BY r.uploadedAt DESC")
    List<Resource> findAllWithClassesByClassId(@Param("classId") Long classId);
}