package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.Internship;

/**
 * Repository for internship data access.
 * Handles actions like delete/edit/add and queries on internships.
 * Single Responsibility: Data access and retrieval only.
 */
public class InternshipRepository {
    private ArrayList<Internship> internshipList;
    private InternshipPersistence persistence;

    public InternshipRepository(InternshipPersistence persistence) {
        this.persistence = persistence;
        this.internshipList = new ArrayList<>(persistence.load());
    }

    /**
     * Add a new internship to the repository
     */
    public void add(Internship internship) {
        internshipList.add(internship);
    }

    /**
     * Find internship by ID
     */
    public Internship findById(String internshipId) {
        if (internshipList.isEmpty()) {
            return null;
        }
        return internshipList.stream()
            .filter(i -> i.getInternshipID().equals(internshipId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Update an existing internship
     */
    public boolean update(Internship updatedInternship) {
        Internship existing = findById(updatedInternship.getInternshipID());
        if (existing == null) {
            return false;
        }
        int index = internshipList.indexOf(existing);
        internshipList.set(index, updatedInternship);
        return true;
    }

    /**
     * Delete an internship
     */
    public boolean delete(String internshipId) {
        Internship internship = findById(internshipId);
        if (internship == null) {
            return false;
        }
        internshipList.remove(internship);
        return true;
    }

    /**
     * Get all internships
     */
    public List<Internship> getAll() {
        return new ArrayList<>(internshipList);
    }

    /**
     * Get all internships for a specific company representative
     */
    public List<Internship> getByRepresentative(String companyRepId) {
        return internshipList.stream()
            .filter(i -> i.getCompanyRepId().equals(companyRepId))
            .collect(Collectors.toList());
    }

    /**
     * Get count of internships for a representative
     */
    public int getCountByRepresentative(String companyRepId) {
        return (int) internshipList.stream()
            .filter(i -> i.getCompanyRepId().equals(companyRepId))
            .count();
    }

    /**
     * Save all internships to persistent storage
     */
    public void save() {
        persistence.save(internshipList);
    }
}
