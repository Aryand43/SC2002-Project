package managers;
import controllers.InternshipFileHandler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import models.Internship;
import models.Internship.InternshipLevel;
import models.Internship.InternshipStatus;


public class InternshipManager {
    private List<Internship> internshipList;
    private InternshipFileHandler fileHandler;
    private static int maxListingsPerRep = 5;


    public InternshipManager(InternshipFileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.internshipList = fileHandler.readFromFile();
    }
    
    /**
     * Function to create internship listing<br>
     * Only able to create a successful listing when:<br>
     * <ol>
     * <li> Company Representative has created less than 5 listings</li>
     * <li> Closing date is AFTER opening date</li>
     * <li> Number of total slots is in the range of 1-10</li>
     * 
     */
    public Internship createOpportunity(
            String companyRepId,
            String title,
            String description,
            InternshipLevel level,
            String preferredMajor,
            LocalDate openingDate,
            LocalDate closingDate,
            String companyName,
            int totalSlots) {
        
        // Check if representative has reached the limit
        if (getInternshipCountByRep(companyRepId) >= maxListingsPerRep) {
            System.out.println("\nError: Maximum of " + maxListingsPerRep + " internships reached.");
            return null;
        }

        // Validate dates
        if (closingDate.isBefore(openingDate)) {
            System.out.println("Error: Closing date cannot be before opening date.");
            return null;
        }

        // Validate slots
        if (totalSlots < 1 || totalSlots > 10) {
            System.out.println("Error: Number of slots must be between 1 and 10.");
            return null;
        }

        Internship internship = new Internship(
            title, description, level, preferredMajor,
            openingDate, closingDate, companyName, companyRepId, totalSlots
        );
    
        System.out.println("Internship created successfully! ID: " + internship.getInternshipID());
        internshipList.add(internship);
        fileHandler.addToFile(internship);
        return internship;
    }

    /**
     * Save internships to file
     */
    public void saveInternships() {
        try {
            fileHandler.writeToFile(new ArrayList<>(internshipList));
        } catch (Exception e) {
            System.err.println("Error saving internships: " + e.getMessage());
        }
    }

    /**
     * Find internship by ID
     */
    public Internship findInternshipByID(String internshipId) {
        if (internshipList.isEmpty()){
            System.out.println("NO internship listings");
        }
        return internshipList.stream()
            .filter(i -> i.getInternshipID().equals(internshipId))
            .findFirst()
            .orElse(null);
    }
    /**
     * Approve the internship (Career Center Staff action)
     */
    public void updateInternshipStatus(String internshipID, String update) {
        Internship i = findInternshipByID(internshipID);
        if (i.getStatus() == InternshipStatus.PENDING && update.equalsIgnoreCase("APPROVED")) {
            i.setStatus(InternshipStatus.APPROVED);
        }
    }

    /**
     * Update an existing internship details
     */
    public boolean updateInternship(Internship updatedInternship) {
        Internship existing = findInternshipByID(updatedInternship.getInternshipID());
        if (existing == null) {
            System.out.println("Error: Internship not found.");
            return false;
        }

        // Update in memory list
        int index = internshipList.indexOf(existing);
        internshipList.set(index, updatedInternship);
        
        // Update in file
        fileHandler.updateInFile(updatedInternship);
        System.out.println("Internship updated successfully.");
        return true;
    }

    /**
 * Get pending internships for approval (Career Center Staff action)
 */
public ArrayList<Internship> getPendingInternships() {
    ArrayList<Internship> pendingInternships = new ArrayList<>();
    
    // Filter internships with PENDING status
    for (Internship internship : internshipList) {
        if (internship.getStatus() == InternshipStatus.PENDING) {
            pendingInternships.add(internship);
        }
    }
    
    // Sort by title (alphabetical order)
    pendingInternships.sort(new Comparator<Internship>() {
        @Override
        public int compare(Internship i1, Internship i2) {
            return i1.getInternshipID().compareTo(i2.getInternshipID());
        }
    });
    
    // Print the pending internships
    if (pendingInternships.isEmpty()) {
        System.out.println("No pending internships found.");
    } else {
        System.out.println("\n======== Pending Internships ========");
        for (int i = 0; i < pendingInternships.size(); i++) {
            System.out.println((i + 1) + ". " + pendingInternships.get(i).getDetailedInfo());
        }
        System.out.println("Total pending: " + pendingInternships.size());
    }
    
    return pendingInternships;
}

     /**
     * @return Number of internship listings made by 1 representative. (Maximum for 1 rep = 5)
     */
    public int getInternshipCountByRep(String companyRepId) {
        return (int) internshipList.stream()
            .filter(i -> i.getCompanyRepId().equals(companyRepId))
            .count();
    }

    /**
     * Getter Function <br>
     * Get all internship listings for Career Center Staff
     */
    public List<Internship> getAllInternships() {
        return new ArrayList<>(internshipList);
    }

    /**
     * Getter Function <br>
     * Get all internships for a specific company representative
     */
    public List<Internship> getInternshipsByRep(String companyRepId) {
        return internshipList.stream()
            .filter(i -> i.getCompanyRepId().equals(companyRepId))
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Getter Function <br>
     * Get visible internships for students based on eligibility & major. <br>
     * Students should only be able to see internships based on thier own major
     */
    public List<Internship> getVisibleInternshipsForStudent(int yearOfStudy, String major) {
        return internshipList.stream()
            .filter(Internship::isVisible)
            .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
            .filter(i -> i.isStudentEligible(yearOfStudy))
            .filter(i -> i.isOpenForApplications())
            .filter(i -> i.getPreferredMajor().equalsIgnoreCase(major))
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Approve an internship listing opportunity (by career center staff only) <br>
     * @return true only when internshp id is valid and the listing is in pending state.
     */
    public boolean approveListing(String internshipId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship == null) {
            System.out.println("Error: Internship listing not found.");
            return false;
        }

        if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println("Error: Only pending internships can be approved.");
            return false;
        }

        internship.setStatus(InternshipStatus.APPROVED);
        fileHandler.updateInFile(internship);
        System.out.println("Internship approved successfully!");
        return true;
    }

    /**
     * Reject an internship listing
     */
    public boolean rejectListing(String internshipId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return false;
        }

        if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println("Error: Only pending internships can be rejected.");
            return false;
        }

        internship.setStatus(InternshipStatus.REJECTED);
        fileHandler.updateInFile(internship);
        System.out.println("Internship rejected.");
        return true;

    }

    /**
     * Toggle visibility of an internship
     */
    public boolean toggleVisibility(String internshipId, String companyRepId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return false;
        }

        // Verify ownership
        if (!internship.getCompanyRepId().equals(companyRepId)) {
            System.out.println("Error: You can only modify your own internships.");
            return false;
        }

        if (internship.isVisible()) {
            internship.toggleVisibility();
            fileHandler.updateInFile(internship);
            System.out.println("Visibility toggled. Now: " + (internship.isVisible() ? "Visible" : "Hidden"));
            return true;
        } else {
            System.out.println("Error: Internship must be approved before changing visibility.");
            return false;
        }
    }

    /**
     * Update internship when slot is confirmed
     */
    public void updateListingOnConfirmation(String internshipId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship != null) {
            internship.incrementConfirmedSlots();
            //UPDATE SLOTS IN FILE!!
            fileHandler.updateInFile(internship);
        }
    }

     /**
     * Update internship when withdrawal is approved
     */
    public void updateListingOnWithdrawal(String internshipId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship != null) {
            internship.decrementConfirmedSlots();
            //UPDATE SLOTS IN FILE!!
            fileHandler.updateInFile(internship);
        }
    }

    /**
     * Delete an internship
     */
    public boolean deleteInternship(String internshipId, String companyRepId) {
        Internship internship = findInternshipByID(internshipId);
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

        internshipList.remove(internship);
        fileHandler.removeFromFile(internshipId);
        System.out.println("Internship deleted successfully.");
        return true;
    }

    /**
     * Filter internships by various criteria
     */
    public List<Internship> filterInternships(
            InternshipStatus status,
            InternshipLevel level,
            String major,
            String companyName) {
        
        return internshipList.stream()
            .filter(i -> status == null || i.getStatus() == status)
            .filter(i -> level == null || i.getLevel() == level)
            .filter(i -> major == null || i.getPreferredMajor().equalsIgnoreCase(major))
            .filter(i -> companyName == null || i.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Display internships in a formatted list
     */
    public void displayInternships() {
        if (internshipList.isEmpty()) {
            System.out.println("No internships found.");
            return;
        }

        System.out.println("\n=== Internship Opportunities ===");
        for (int i = 0; i < internshipList.size(); i++) {
            System.out.println((i + 1) + ". " + internshipList.get(i).getDetailedInfo());
        }
        System.out.println();
    }
}