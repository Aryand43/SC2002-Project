package boundaries;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import models.*;
import models.Internship.InternshipLevel;
import models.Internship.InternshipStatus;

/**
 * Boundary class for handling all user interactions related to internships
 */
public class InternshipBoundary {
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public InternshipBoundary(Scanner scanner) {
        this.scanner = scanner;
    }
    
    // ==================== DISPLAY METHODS ====================
    
    /**
     * Display a list of internships in formatted manner
     */
    public void displayInternshipList(List<Internship> internships, String title) {
        printSectionHeader(title);
        
        if (internships.isEmpty()) {
            System.out.println("No internships found.");
            return;
        }
        
        for (int i = 0; i < internships.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), internships.get(i).getDetailedInfo());
        }
        
        System.out.println("\nTotal: " + internships.size() + " internship(s)");
        printSeparator();
    }
    
    /**
     * Display detailed information about a single internship
     */
    public void displayInternshipDetails(Internship internship) {
        if (internship == null) {
            System.out.println("Internship not found.");
            return;
        }
        
        printSectionHeader("Internship Details");
        System.out.println(internship.getDetailedInfo());
        printSeparator();
    }
    
    /**
     * Display internships available to a student
     */
    public void displayStudentInternships(List<Internship> internships) {
        displayInternshipList(internships, "Available Internship Opportunities");
    }
    
    /**
     * Display internships created by a company representative
     */
    public void displayCompanyRepInternships(List<Internship> internships, String companyName) {
        displayInternshipList(internships, "Your Internships - " + companyName);
    }
    
    /**
     * Display pending internships to approve for Career Center Staff
     */
    public void displayPendingInternships(List<Internship> internships) {
        displayInternshipList(internships, "Pending Internship Approvals");
    }
    
    /**
     * Display search results
     */
    public void displaySearchResults(List<Internship> results, String keyword) {
        String title = String.format("Search Results for: \"%s\"", keyword);
        displayInternshipList(results, title);
    }
    
    // ==================== INPUT METHODS ====================
    
    /**
     * Get internship creation input from the Company Representative.<br>
     * Need to pass in the Company Representative user object into the function
     */
    public Internship internshipCreation(CompanyRepresentative u) {
        printSectionHeader("Create New Internship Opportunity");
        
        System.out.print("Enter internship title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();
        
        InternshipLevel level = selectInternshipLevel();
        
        System.out.print("Enter preferred major (e.g., CSC, EEE, MAE): ");
        String preferredMajor = scanner.nextLine().trim().toUpperCase();
        
        LocalDate openingDate = inputDate("Enter opening date (dd/MM/yyyy): ");
        LocalDate closingDate = inputDate("Enter closing date (dd/MM/yyyy): ");
        
        int totalSlots = inputInteger("Enter number of slots (1-10): ", 1, 10);
        
        printSeparator();
        
        return new Internship(title, description, level, preferredMajor, openingDate, closingDate, u.getCompanyName(), u.getID(), totalSlots);
    }
    
    /**
     * Select an internship from a list
     */
    public int selectInternshipFromList(List<Internship> internships, String prompt) {
        if (internships.isEmpty()) {
            System.out.println("No internships available to select.");
            return -1;
        }
        
        displayInternshipList(internships, "Select Internship");
        
        int choice = inputInteger(prompt + " (0 to cancel): ", 0, internships.size());
        
        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return -1;
        }
        
        return choice - 1; // Return index
    }
    
    /**
     * Get search keyword from user
     */
    public String getSearchKeyword() {
        System.out.print("Enter search keyword: ");
        return scanner.nextLine().trim();
    }
    
    /**
     * Select internship level
     */
    public InternshipLevel selectInternshipLevel() {
        System.out.println("\nSelect Internship Level:");
        System.out.println("1. BASIC");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");
        
        int choice = inputInteger("Enter choice (1-3): ", 1, 3);
        
        switch (choice) {
            case 1: return InternshipLevel.BASIC;
            case 2: return InternshipLevel.INTERMEDIATE;
            case 3: return InternshipLevel.ADVANCED;
            default: return InternshipLevel.BASIC;
        }
    }
    
    /**
     * Select internship status for filtering
     */
    public InternshipStatus selectInternshipStatus() {
        System.out.println("\nSelect Internship Status:");
        System.out.println("1. PENDING");
        System.out.println("2. APPROVED");
        System.out.println("3. REJECTED");
        System.out.println("4. FILLED");
        System.out.println("5. ALL (No filter)");
        
        int choice = inputInteger("Enter choice (1-5): ", 1, 5);
        
        switch (choice) {
            case 1: return InternshipStatus.PENDING;
            case 2: return InternshipStatus.APPROVED;
            case 3: return InternshipStatus.REJECTED;
            case 4: return InternshipStatus.FILLED;
            case 5: return null; // No filter
            default: return null;
        }
    }
    
    /**
     * Get filter criteria from user
     */
    public FilterCriteria getFilterCriteria() {
        printSectionHeader("Filter Internships");
        
        System.out.println("Apply filters (press Enter to skip):");
        
        System.out.print("Status (PENDING/APPROVED/REJECTED/FILLED) or leave blank: ");
        String statusInput = scanner.nextLine().trim();
        InternshipStatus status = statusInput.isEmpty() ? null : InternshipStatus.valueOf(statusInput.toUpperCase());
        
        System.out.print("Level (BASIC/INTERMEDIATE/ADVANCED) or leave blank: ");
        String levelInput = scanner.nextLine().trim();
        InternshipLevel level = levelInput.isEmpty() ? null : InternshipLevel.valueOf(levelInput.toUpperCase());
        
        System.out.print("Major (e.g., CSC, EEE) or leave blank: ");
        String major = scanner.nextLine().trim();
        major = major.isEmpty() ? null : major;
        
        System.out.print("Company name (partial match) or leave blank: ");
        String companyName = scanner.nextLine().trim();
        companyName = companyName.isEmpty() ? null : companyName;
        
        printSeparator();
        
        return new FilterCriteria(status, level, major, companyName);
    }
    
    // ==================== CONFIRMATION METHODS ====================
    
    /**
     * Confirm an action with yes/no
     */
    public boolean confirmAction(String message) {
        System.out.print(message + " (Y/N): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }
    
    /**
     * Confirm internship approval
     */
    public boolean confirmApproval(Internship internship) {
        displayInternshipDetails(internship);
        return confirmAction("Do you want to APPROVE this internship?");
    }
    
    /**
     * Confirm internship rejection
     */
    public boolean confirmRejection(Internship internship) {
        displayInternshipDetails(internship);
        return confirmAction("Do you want to REJECT this internship?");
    }
    
    /**
     * Confirm application
     */
    public boolean confirmApplication(Internship internship) {
        displayInternshipDetails(internship);
        return confirmAction("Do you want to apply for this internship?");
    }
    
    // ==================== MESSAGE METHODS ====================
    
    /**
     * Display success message
     */
    public void displaySuccessMessage(String message) {
        System.out.println("\n✓ SUCCESS: " + message);
    }
    
    /**
     * Display error message
     */
    public void displayErrorMessage(String message) {
        System.out.println("\n✗ ERROR: " + message);
    }
    
    /**
     * Display info message
     */
    public void displayInfoMessage(String message) {
        System.out.println("\nℹ INFO: " + message);
    }
    
    /**
     * Display warning message
     */
    public void displayWarningMessage(String message) {
        System.out.println("\n⚠ WARNING: " + message);
    }
    
    // ==================== MENU METHODS ====================
    
    /**
     * Display Student Internship Menu
     */
    public int displayStudentInternshipMenu() {
        printSectionHeader("Student Internship Menu");
        System.out.println("1. View Available Internships");
        System.out.println("2. Search Internships");
        System.out.println("3. Apply for Internship");
        System.out.println("4. View My Applications");
        System.out.println("5. Filter Internships");
        System.out.println("0. Back to Main Menu");
        printSeparator();
        
        return inputInteger("Enter your choice: ", 0, 5);
    }
    
    /**
     * Display Company Representative Internship Menu
     */
    public int displayCompanyRepInternshipMenu() {
        printSectionHeader("Company Representative - Internship Management");
        System.out.println("1. View My Internships");
        System.out.println("2. Create New Internship");
        System.out.println("3. Toggle Internship Visibility");
        System.out.println("4. View Applications");
        System.out.println("5. Approve/Reject Applications");
        System.out.println("0. Back to Main Menu");
        printSeparator();
        
        return inputInteger("Enter your choice: ", 0, 5);
    }
    
    /**
     * Display Career Center Staff Internship Menu
     */
    public int displayStaffInternshipMenu() {
        printSectionHeader("Career Center Staff - Internship Management");
        System.out.println("1. View All Internships");
        System.out.println("2. View Pending Internships");
        System.out.println("3. Approve Internship");
        System.out.println("4. Reject Internship");
        System.out.println("5. Filter Internships");
        System.out.println("6. Generate Report");
        System.out.println("0. Back to Main Menu");
        printSeparator();
        
        return inputInteger("Enter your choice: ", 0, 6);
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Input an integer with validation
     */
    private int inputInteger(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Input a date with validation
     */
    private LocalDate inputDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateInput = scanner.nextLine().trim();
                return LocalDate.parse(dateInput, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy (e.g., 25/12/2024).");
            }
        }
    }
    
    /**
     * Print section header
     */
    private void printSectionHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }
    
    /**
     * Print separator line
     */
    private void printSeparator() {
        System.out.println("-".repeat(60));
    }
    
    /**
     * Pause and wait for user to press Enter
     */
    public void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // ==================== INNER CLASSES FOR DATA TRANSFER ====================
    
    /**
     * Data class for internship creation
     */
    public static class InternshipCreationData {
        public final String title;
        public final String description;
        public final InternshipLevel level;
        public final String preferredMajor;
        public final LocalDate openingDate;
        public final LocalDate closingDate;
        public final int totalSlots;
        
        public InternshipCreationData(String title, String description, InternshipLevel level,
                                     String preferredMajor, LocalDate openingDate,
                                     LocalDate closingDate, int totalSlots) {
            this.title = title;
            this.description = description;
            this.level = level;
            this.preferredMajor = preferredMajor;
            this.openingDate = openingDate;
            this.closingDate = closingDate;
            this.totalSlots = totalSlots;
        }
    }
    
    /**
     * Data class for filter criteria
     */
    public static class FilterCriteria {
        public final InternshipStatus status;
        public final InternshipLevel level;
        public final String major;
        public final String companyName;
        
        public FilterCriteria(InternshipStatus status, InternshipLevel level,
                            String major, String companyName) {
            this.status = status;
            this.level = level;
            this.major = major;
            this.companyName = companyName;
        }
    }
}