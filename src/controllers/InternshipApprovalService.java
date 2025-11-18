package controllers;

import models.Internship;
import models.Internship.InternshipStatus;

/**
 * Handles approval and rejection workflows for internship listings.
 * Single Responsibility: Internship approval/rejection logic.
 * Open/Closed: New approval workflows can be added by extending or composing with this class.
 */
public class InternshipApprovalService {
    private InternshipRepository repository;

    public InternshipApprovalService(InternshipRepository repository) {
        this.repository = repository;
    }

    /**
     * Approve a pending internship listing (Staff action)
     * @param internshipId ID of the internship to approve
     * @return true if successful, false if internship not found or not in PENDING state
     */
    public boolean approveListing(String internshipId) {
        Internship internship = repository.findById(internshipId);
        if (internship == null) {
            throw new InternshipNotFoundException("Internship listing not found: " + internshipId);
        }

        if (internship.getStatus() != InternshipStatus.PENDING) {
            throw new InvalidStateException("Only pending internships can be approved. Current status: " + internship.getStatus());
        }

        internship.setStatus(InternshipStatus.APPROVED);
        return true;
    }

    /**
     * Reject a pending internship listing (Staff action)
     * @param internshipId ID of the internship to reject
     * @return true if successful, false if internship not found or not in PENDING state
     */
    public boolean rejectListing(String internshipId) {
        Internship internship = repository.findById(internshipId);
        if (internship == null) {
            throw new InternshipNotFoundException("Internship listing not found: " + internshipId);
        }

        if (internship.getStatus() != InternshipStatus.PENDING) {
            throw new InvalidStateException("Only pending internships can be rejected. Current status: " + internship.getStatus());
        }

        internship.setStatus(InternshipStatus.REJECTED);
        return true;
    }

    /**
     * Change the status of an internship listing
     * @param internshipId ID of the internship
     * @param newStatus The new status to set
     */
    public void changeStatus(String internshipId, InternshipStatus newStatus) {
        Internship internship = repository.findById(internshipId);
        if (internship == null) {
            throw new InternshipNotFoundException("Internship listing not found: " + internshipId);
        }
        internship.setStatus(newStatus);
    }

    /**
     * Custom exception for when internship is not found
     */
    public static class InternshipNotFoundException extends RuntimeException {
        public InternshipNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception for invalid state transitions
     */
    public static class InvalidStateException extends RuntimeException {
        public InvalidStateException(String message) {
            super(message);
        }
    }
}
