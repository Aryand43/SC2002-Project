package controllers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import models.Internship;
import models.Internship.InternshipLevel;
import models.Internship.InternshipStatus;

/**
 * Handles read-only query and filtering operations on internships.<br>
 * Single Responsibility: Search, filter, and query internships.
 */
public class InternshipQueryService {
    private InternshipRepository repository;

    public InternshipQueryService(InternshipRepository repository) {
        this.repository = repository;
    }

    /**
     * Get internships pending approval (for staff review)
     */
    public List<Internship> getPendingInternships() {
        return repository.getAll().stream()
            .filter(i -> i.getStatus() == InternshipStatus.PENDING)
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Get internships visible and available for students
     */
    public List<Internship> getVisibleInternshipsForStudent(int yearOfStudy, String major) {
        return repository.getAll().stream()
            .filter(Internship::isVisible)
            .filter(i -> !i.isFull())
            .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
            .filter(i -> i.isStudentEligible(yearOfStudy))
            .filter(i -> i.isOpenForApplications())
            .filter(i -> i.getPreferredMajor().equalsIgnoreCase(major))
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Filter internships by multiple criteria
     */
    public List<Internship> filterInternships(
            InternshipStatus status,
            InternshipLevel level,
            String major,
            String companyName) {
        
        return repository.getAll().stream()
            .filter(i -> status == null || i.getStatus() == status)
            .filter(i -> level == null || i.getLevel() == level)
            .filter(i -> major == null || i.getPreferredMajor().equalsIgnoreCase(major))
            .filter(i -> companyName == null || i.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Search internships by keyword in title and description
     */
    public List<Internship> search(String keyword) {
        String searchTerm = keyword.toLowerCase().trim();
        
        return repository.getAll().stream()
            .filter(i -> i.getTitle().toLowerCase().contains(searchTerm) 
                    || i.getDescription().toLowerCase().contains(searchTerm))
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Get all internships
     */
    public List<Internship> getAllInternships() {
        return repository.getAll();
    }
}
