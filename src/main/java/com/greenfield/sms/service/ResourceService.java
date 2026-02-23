package com.greenfield.sms.service;

import com.greenfield.sms.model.Resource;
import com.greenfield.sms.model.Classes;
import com.greenfield.sms.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing Resources in Greenfield SMS.
 * Provides methods to fetch, save, and delete resources safely.
 */
@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    // ================= FETCH ALL RESOURCES =================
    /**
     * Fetch all resources with their associated classes eagerly loaded,
     * sorted by uploadedAt descending.
     * @return List of resources, empty if none found
     */
    public List<Resource> getAllResources() {
        List<Resource> resources = resourceRepository.findAllWithClasses();
        return resources != null ? resources : Collections.emptyList();
    }

    /**
     * Fetch all resources assigned to a specific class (eagerly loaded)
     * @param classes the class entity
     * @return List of resources, empty if none found
     */
    public List<Resource> getResourcesByClass(Classes classes) {
        if (classes == null) return Collections.emptyList();
        return resourceRepository.findByClassesOrderByUploadedAtDesc(classes);
    }

    /**
     * Fetch resources by class ID (eagerly fetch associated class)
     * @param classId ID of the class
     * @return List of resources, empty if none found
     */
    public List<Resource> getResourcesByClassId(Long classId) {
        if (classId == null) return Collections.emptyList();
        return resourceRepository.findByClassesIdOrderByUploadedAtDesc(classId);
    }

    // ================= SAVE RESOURCE =================
    /**
     * Save or update a resource.
     * Throws IllegalArgumentException if the resource is null.
     * @param resource the resource entity to save
     * @return the saved resource
     */
    public Resource saveResource(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Cannot save null resource.");
        }
        return resourceRepository.save(resource);
    }

    // ================= DELETE RESOURCE =================
    /**
     * Delete a resource by ID safely.
     * Throws exception if resource does not exist.
     * @param id the resource ID
     */
    public void deleteResource(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Resource ID cannot be null.");
        }
        Optional<Resource> resourceOpt = resourceRepository.findById(id);
        if (resourceOpt.isPresent()) {
            resourceRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Resource with ID " + id + " does not exist.");
        }
    }

    // ================= FETCH SINGLE RESOURCE =================
    /**
     * Fetch a single resource by its ID safely.
     * Returns null if ID is null or resource does not exist.
     * @param id the resource ID
     * @return the resource or null
     */
    public Resource getResourceById(Long id) {
        if (id == null) return null;
        return resourceRepository.findById(id).orElse(null);
    }
}