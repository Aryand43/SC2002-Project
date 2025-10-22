package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Internship {
    public enum InternshipLevel {BASIC, INTERMEDIATE, ADVANCED};
    public enum InternshipStatus {PENDING, APPROVED, REJECTED, FILLED};
    private static int idCounter = 1;

    protected String internshipId;
    protected String title;
    protected String description;
    protected InternshipLevel level;
    protected String preferredMajor;
    protected LocalDate openingDate;
    protected LocalDate closingDate;
    protected InternshipStatus status;
    protected String companyName;
    protected String companyRepId;
    protected int totalSlots;
    protected int availableSlots;
    protected int confirmedSlots;
    protected boolean visible;
    
   public Internship(
        String title, 
        String description,
        InternshipLevel level,
        String preferredMajor,
        LocalDate openingDate,
        LocalDate closingDate,
        String companyName,
        String companyRepId,
        int totalSlots) {
        
            this.internshipId = generateInternshipId();
            this.title = title;
            this.description = description;
            this.level = level;
            this.preferredMajor = preferredMajor;
            this.openingDate = openingDate;
            this.closingDate = closingDate;
            this.status = InternshipStatus.PENDING;
            this.companyName = companyName;
            this.companyRepId = companyRepId;
            this.totalSlots = totalSlots;
            this.availableSlots = totalSlots;
            this.confirmedSlots = 0;
            this.visible = false;
        }
    
    /**
     * Generates an Internship ID that starts with INT padded with 0's if idCOunter is less than 4 digits
     * @return an unique ID for each internship listing
     */
    private static String generateInternshipId() {
        return "INT" + String.format("%04d", idCounter++);
    }

    /**
     * Getters
     */
    public String getInternshipID() { return internshipId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public InternshipLevel getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public InternshipStatus getStatus() { return status; }
    public String getCompanyName() { return companyName; }
    public String getCompanyRepId() { return companyRepId; }
    public int getTotalSlots() { return totalSlots; }
    public int getAvailableSlots() { return availableSlots; }
    public int getConfirmedSlots() { return confirmedSlots; }
    public boolean isVisible() { return visible; }

    /**
     * Setters
     */
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLevel(InternshipLevel level) { this.level = level; }
    public void setPreferredMajor(String preferredMajor) { this.preferredMajor = preferredMajor; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }
    public void setStatus(InternshipStatus status) { this.status = status; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setVisible(boolean visible) { this.visible = visible; }

    /**
     * Toggle visibility of internship listing. Only visible if status is APPROVED
     * @return 
     * <ol>
     * <li>true as long as the Internship Status is "APPROVED".<br>
     * Visibility is able to be changed from true to false and false to true</li>
     * <li>false if internship listing is anything other than "APPROVED" </li>
     * </ol>
     */
    public boolean toggleVisibility() {
        if (this.status != InternshipStatus.APPROVED) {
            return false;
        }
        this.visible = !this.visible;
        return true;
    }

    /**
     * Check if internship is open for applications. IF date is before opening date, listing should not be visible.<br>
     * If date is after clsoing date, listing should also be hidden. The lsiting is only visible if within closing and opening date. 
     */
    public boolean isOpenForApplications() {
        LocalDate today = LocalDate.now();
        return this.status == InternshipStatus.APPROVED &&
               this.visible &&
               !today.isBefore(openingDate) &&
               !today.isAfter(closingDate) &&
               this.availableSlots > 0 &&
               this.status != InternshipStatus.FILLED;
    }

    /**
     * Checks if student is elgible to apply for this specific internship. Based on requirements:<br>
     * <ul>
     * <li>Year 1 and 2 students can ONLY apply for BASIC internships.</li>
     * <li>Year 3 and above students can apply for ALL levels (Basic, Intermediate, Advanced).
     * <li>Students can only see internships of the SAME major
     */
    public boolean isStudentEligible(int yearOfStudy) {
        // Check eligibility based on year of study
        if (yearOfStudy <= 2 && this.level != InternshipLevel.BASIC) {
            return false;
        }
        return true;
    }


    /**
     * Increment confirmed slots when student accepts placement
     */
    public void incrementConfirmedSlots() {
        if (confirmedSlots < totalSlots) {
            confirmedSlots++;
            availableSlots--;
            
            // Update status to FILLED if all slots are confirmed
            if (confirmedSlots == totalSlots) {
                this.status = InternshipStatus.FILLED;
            }
        }
    }

    /**
     * Decrement confirmed slots when a student's withdrawal is approved.<br>
     * Also checks if the withdrawal happened to open up the listing again
     */
    public void decrementConfirmedSlots() {
        if (confirmedSlots > 0) {
            confirmedSlots--;
            availableSlots++;
            
            // Update status from FILLED back to APPROVED if withdrawal happened to be the last slot
            if (this.getStatus() == InternshipStatus.FILLED && this.getAvailableSlots() > 0) {
                this.status = InternshipStatus.APPROVED;
            }
        }
    }

    /**
     * Get detailed information of internship listing
     */
    public String getDetailedInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("=== Internship Details ===\n");
        sb.append("ID: ").append(internshipId).append("\n");
        sb.append("Title: ").append(title).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Company: ").append(companyName).append("\n");
        sb.append("Level: ").append(level).append("\n");
        sb.append("Preferred Major: ").append(preferredMajor).append("\n");
        sb.append("Opening Date: ").append(openingDate.format(formatter)).append("\n");
        sb.append("Closing Date: ").append(closingDate.format(formatter)).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Available Slots: ").append(availableSlots).append("/").append(totalSlots).append("\n");
        sb.append("Confirmed Slots: ").append(confirmedSlots).append("\n");
        sb.append("Visible to Students: ").append(visible ? "Yes" : "No").append("\n");
        return sb.toString();
    }
}
