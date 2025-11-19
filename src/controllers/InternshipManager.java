package controllers;

import java.time.LocalDate;
import java.util.List;
import models.Internship;
import models.Internship.InternshipLevel;
import models.Internship.InternshipStatus;

/**
 * InternshipManager coordinates between the @see InternshipRepository, @see InternshipApprovalService, and @see InternshipQueryService.<br>
 * The requirements are extracted to policies (@see StandardPolicy which implements @see ListingPolicy) that can be swapped out for different implementations.<br>
 * Depends on interfaces, not concrete implementations (Dependency Inversion).
 */
public class InternshipManager {
    private InternshipRepository repository;
    private InternshipApprovalService approvalService;
    private InternshipQueryService queryService;
    private ListingPolicy listingPolicy;

    public InternshipManager() {
        DataPersistence<Internship> persistence = new FileInternshipPersistence();
        this.repository = new InternshipRepository(persistence);
        this.approvalService = new InternshipApprovalService(repository);
        this.queryService = new InternshipQueryService(repository);
        this.listingPolicy = new StandardListingPolicy();
    }

    /**
     * Constructor for testing or custom configurations
     */
    public InternshipManager(
            InternshipRepository repository,
            InternshipApprovalService approvalService,
            InternshipQueryService queryService,
            ListingPolicy listingPolicy) {
        this.repository = repository;
        this.approvalService = approvalService;
        this.queryService = queryService;
        this.listingPolicy = listingPolicy;
    }

    /**
     * Create a new internship listing<br>
     * Validates against the current requirements defined in StandardListingPolicy<br>
     * @return the created internship, or null if validation fails
     */
    public Internship createListing(
            String companyRepId,
            String title,
            String description,
            InternshipLevel level,
            String preferredMajor,
            LocalDate openingDate,
            LocalDate closingDate,
            String companyName,
            int totalSlots, 
            String listingId) {
        
        try {
            // Validate representative listing count against policy
            int currentCount = repository.getCountByRepresentative(companyRepId);
            if (!listingPolicy.canCreateListing(companyRepId, currentCount)) {
                System.out.println("\nError: Maximum of " + listingPolicy.getMaxListingsPerRep() + " internships reached.");
                return null;
            }

            // Validate dates
            if (closingDate.isBefore(openingDate)) {
                System.out.println("Error: Closing date cannot be before opening date.");
                return null;
            }

            // Validate slots against policy
            if (!listingPolicy.isValidSlotCount(totalSlots)) {
                System.out.println("Error: Number of slots must be between " + 
                    listingPolicy.getMinSlots() + " and " + listingPolicy.getMaxSlots() + ".");
                return null;
            }

            Internship internship = new Internship(
                listingId, title, description, level, preferredMajor,
                openingDate, closingDate, companyName, companyRepId, totalSlots
            );

            repository.add(internship);
            System.out.println("Internship created successfully! ID: " + internship.getInternshipID());
            return internship;

        } catch (Exception e) {
            System.out.println("Error creating internship: " + e.getMessage());
            return null;
        }
    }

    /**
     * Find internship by ID
     */
    public Internship findInternshipByID(String internshipId) {
        return repository.findById(internshipId);
    }

    /**
     * Update an existing internship
     */
    public boolean updateInternship(Internship updatedInternship) {
        try {
            return repository.update(updatedInternship);
        } catch (Exception e) {
            System.out.println("Error updating internship: " + e.getMessage());
            return false;
        }
    }

    /**
     * Change the status of listing, using @see InternshipApprovalService
     */
    public void changeInternshipStatus(String internshipID, InternshipStatus update) {
        try {
            approvalService.changeStatus(internshipID, update);
        } catch (Exception e) {
            System.out.println("Error changing status: " + e.getMessage());
        }
    }

    /**
     * Get pending internships, using @see InternshipQueryService
     */
    public List<Internship> getPendingInternships() {
        return queryService.getPendingInternships();
    }

    /**
     * Get count of internships by representative
     */
    public int getInternshipCountByRep(String companyRepId) {
        return repository.getCountByRepresentative(companyRepId);
    }

    /**
     * Get all internships
     */
    public List<Internship> getAllInternships() {
        return repository.getAll();
    }

    /**
     * Get internships by representative
     */
    public List<Internship> getInternshipsByRep(String companyRepId) {
        return repository.getByRepresentative(companyRepId);
    }

    /**
     * Get visible internships for students, using @see InternshipQueryService
     */
    public List<Internship> getVisibleInternshipsForStudent(int yearOfStudy, String major) {
        return queryService.getVisibleInternshipsForStudent(yearOfStudy, major);
    }

    /**
     * Approve a listing, using @see InternshipApprovalService
     */
    public boolean approveListing(String internshipId) {
        try {
            return approvalService.approveListing(internshipId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reject a listing, using @see InternshipApprovalService
     */
    public boolean rejectListing(String internshipId) {
        try {
            return approvalService.rejectListing(internshipId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Toggle visibility of internship listing
     */
    public boolean toggleVisibility(String internshipId, String companyRepId) {
        try {
            Internship internship = repository.findById(internshipId);
            if (internship == null) {
                System.out.println("Error: Internship not found.");
                return false;
            }

            // Verify status
            if (internship.getStatus() != InternshipStatus.APPROVED) {
                System.out.println("Error: Internship must be approved before changing visibility.");
                return false;
            }

            // Verify ownership
            if (!internship.getCompanyRepId().equals(companyRepId)) {
                System.out.println("Error: You can only modify your own internships.");
                return false;
            }

            if (internship.isVisible()) {
                internship.setInvisible();
                System.out.println("Visibility toggled. Now: Hidden");
            } else {
                internship.setVisible();
                System.out.println("Visibility toggled. Now: Visible");
            }
            return true;

        } catch (Exception e) {
            System.out.println("Error toggling visibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update internship when slot is confirmed 
     */
    public void updateListingOnConfirmation(String internshipId) {
        try {
            Internship internship = repository.findById(internshipId);
            if (internship != null) {
                internship.confirmSlot();  // Domain logic encapsulated in Internship
            }
        } catch (Exception e) {
            System.out.println("Error confirming slot: " + e.getMessage());
        }
    }

    /**
     * Update internship when withdrawal is approved
     */
    public void updateListingOnWithdrawal(String internshipId) {
        try {
            Internship internship = repository.findById(internshipId);
            if (internship != null) {
                internship.withdrawSlot();  // Domain logic encapsulated in Internship
            }
        } catch (Exception e) {
            System.out.println("Error withdrawing slot: " + e.getMessage());
        }
    }

    /**
     * Delete an internship
     */
    public boolean deleteInternship(String internshipId, String companyRepId) {
        try {
            Internship internship = repository.findById(internshipId);
            if (internship == null) {
                System.out.println("Error: Internship not found.");
                return false;
            }

            // Verify ownership
            if (!internship.getCompanyRepId().equals(companyRepId)) {
                System.out.println("Error: You can only delete your own internships.");
                return false;
            }

            // Only allow deletion if no confirmed slots
            if (internship.getConfirmedSlots() > 0) {
                System.out.println("Error: Cannot delete internship with confirmed placements.");
                return false;
            }

            repository.delete(internshipId);
            System.out.println("Internship deleted successfully.");
            return true;

        } catch (Exception e) {
            System.out.println("Error deleting internship: " + e.getMessage());
            return false;
        }
    }

    /**
     * Filter internships, using @see InternshipQueryService
     */
    public List<Internship> filterInternships(
            InternshipStatus status,
            InternshipLevel level,
            String major,
            String companyName) {
        return queryService.filterInternships(status, level, major, companyName);
    }

    /**
     * Search internships, using @see InternshipQueryService
     */
    public List<Internship> search(String keyword) {
        return queryService.search(keyword);
    }

    /**
     * Save to file, using @see InternshipRepository
     */
    public void saveToFile() {
        try {
            repository.save();
        } catch (Exception e) {
            System.out.println("Error saving internships: " + e.getMessage());
        }
    }
}
