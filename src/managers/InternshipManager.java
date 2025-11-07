package managers;
import controllers.FileHandler;
import controllers.InternshipSerializer;
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
    private FileHandler fileHandler;
    private static int maxListingsPerRep = 5;


    public InternshipManager() {
    	InternshipSerializer internshipSerializer = new InternshipSerializer();
    	FileHandler<Internship> internshipFileHandler = new FileHandler<>(internshipSerializer);
    	this.fileHandler = internshipFileHandler;
        this.internshipList = internshipFileHandler.readFromFile();
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
    public Internship createListing(
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
        return internship;
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
     * Change the status of listing (Career Center Staff action) <br>
     * Career Center Staff can use this to approve/reject an internship.
     */
    public void changeInternshipStatus(String internshipID, InternshipStatus update) {
        Internship i = findInternshipByID(internshipID);
        i.setStatus(update);
    }

    /**
     * Update an existing internship details
     */
    public boolean updateInternship(Internship updatedInternship) {
        Internship internship = findInternshipByID(updatedInternship.getInternshipID());
        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return false;
        }

        // Update in list
        int index = internshipList.indexOf(internship);
        internshipList.set(index, updatedInternship);
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
        pendingInternships.sort((i1, i2) -> i1.getTitle().compareTo(i2.getTitle()));
        return pendingInternships;
    }

    /**
     * Print pending internships
     */
    public void displayPendingInternships() {
        ArrayList<Internship> pendingInternships = getPendingInternships();
        
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships found.");
        } else {
            System.out.println("\n=== Pending Internships ===");
            for (int i = 0; i < pendingInternships.size(); i++) {
                System.out.println((i + 1) + ". " + pendingInternships.get(i).getDetailedInfo());
            }
            System.out.println("Total pending: " + pendingInternships.size());
        }
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
     * Getter Function to return the stored internship list <br>
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
     * Students should only be able to see internships based on: <br>
     * <ol> 
     * <li>Internship listing is visibl</li>
     * <li>Internship listing still has available slots</li>
     * <li>Internship listing is open and within the opening and closing date</li>
     * <li>thier own major</li>
     * </ol>
     */
    public List<Internship> getVisibleInternshipsForStudent(int yearOfStudy, String major) {
        return internshipList.stream()
            .filter(i -> i.isVisible())
            .filter(i -> i.isFull() == false)
            .filter(i -> i.getStatus() == InternshipStatus.APPROVED)
            .filter(i -> i.isStudentEligible(yearOfStudy))
            .filter(i -> i.isOpenForApplications())
            .filter(i -> i.getPreferredMajor().equalsIgnoreCase(major))
            .sorted(Comparator.comparing(Internship::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Approve an internship listing opportunity (by career center staff only) <br>
     * @return true only when internship id is valid and the listing is in pending state.
     */
    public boolean approveListing(String internshipId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship == null) {
            System.out.println("Error: Internship listing not found.");
            return false;
        }

        else if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println("Error: Only pending internships can be approved.");
            return false;
        }

        internship.setStatus(InternshipStatus.APPROVED);
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

        else if (internship.getStatus() != InternshipStatus.PENDING) {
            System.out.println("Error: Only pending internships can be rejected.");
            return false;
        }

        internship.setStatus(InternshipStatus.REJECTED);
        System.out.println("Internship rejected.");
        return true;

    }

    /**
     * Toggle visibility of internship listing. Only visible if status is APPROVED
     * @return 
     * <ol>
     * <li>true as long as the Internship Status is "APPROVED".<br>
     * Visibility is able to be changed from true to false and false to true</li>
     * <li>false if internship listing is anything other than "APPROVED" </li>
     * </ol>
     */
    public boolean toggleVisibility(String internshipId, String companyRepId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship == null) {
            System.out.println("Error: Internship not found.");
            return false;
        }

        // Verify status
        if (internship.getStatus() != InternshipStatus.APPROVED){
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
            return true;
        } 
        else{
            internship.setVisible();
            System.out.println("Visibility toggled. Now: Visible");
            return true;
        }
    }

    /**
     * Update internship when slot is confirmed
     */
    public void updateListingOnConfirmation(String internshipId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship != null) {
            internship.incrementConfirmedSlots();
            if (internship.getAvailableSlots() == 0) internship.setInvisible();
        }
    }

     /**
     * Update internship when withdrawal is approved
     */
    public void updateListingOnWithdrawal(String internshipId) {
        Internship internship = findInternshipByID(internshipId);
        if (internship != null) {
            internship.decrementConfirmedSlots();
            if (internship.getAvailableSlots() > 0) internship.setVisible();
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
     * Search internships by keyword in title and description
     * @param keyword - the search term to look for
     * @return List of internships that match the keyword
     */
    public List<Internship> search(String keyword) {
        List<Internship> matchingInternships = new ArrayList<>();
        
        // Convert keyword to lowercase for case-insensitive search
        String searchTerm = keyword.toLowerCase().trim();
        
        // Filter internships that contain the keyword in title or description
        for (Internship internship : internshipList) {
            String title = internship.getTitle().toLowerCase();
            String description = internship.getDescription().toLowerCase();
            
            // Check if keyword appears in title or description
            if (title.contains(searchTerm) || description.contains(searchTerm)) {
                matchingInternships.add(internship);
            }
        }
        
        // Sort results by title
        matchingInternships.sort((i1, i2) -> i1.getTitle().compareTo(i2.getTitle()));
        return matchingInternships;
    }
}