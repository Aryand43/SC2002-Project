package boundaries;

import controllers.ApplicationManager;
import controllers.InternshipManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import models.Application;
import models.CompanyRepresentative;
import models.Internship;
import models.Internship.InternshipLevel;


public class CompanyRepBoundary {

    private final Scanner scanner;
    private final CompanyRepresentative currentUser;
    private final InternshipManager internshipManager;
    private final ApplicationManager applicationManager;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CompanyRepBoundary(Scanner scanner, CompanyRepresentative currentUser, InternshipManager internshipManager, ApplicationManager applicationManager) {
        this.scanner = scanner;
        this.currentUser = currentUser;
        this.internshipManager = internshipManager;
        this.applicationManager = applicationManager;
    }
    
    // Helper function for user integer input with bounds
    private int inputInteger(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty.");
                    continue;
                }
                int value = Integer.parseInt(input);
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
    
    private String inputString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private LocalDate inputDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (dd/MM/yyyy): ");
                String dateInput = scanner.nextLine().trim();
                return LocalDate.parse(dateInput, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy (e.g., 25/12/2024).");
            }
        }
    }


    public void run() {
        if (currentUser == null) return;
        
        System.out.println("\nWelcome, Company Representative " + currentUser.getUserName() + " (" + currentUser.getCompanyName() + ")");
        if (currentUser.isApproved() == null) {
            System.out.println("Your account is pending approval by Career Center Staff. You cannot access features yet.");
            return;
        } else if (!currentUser.isApproved()) {
            System.out.println("Your account has been rejected. Please contact Career Center for assistance.");
            return;
        }

        int choice = -1;
        while (choice != 0) {
            new MenuBoundary().displayCompanyRepInternshipMenu();
            choice = inputInteger("Enter choice: ", 0, 5);
            
            switch (choice) {
                case 1 -> viewMyInternships();
                case 2 -> createNewInternship();
                case 3 -> toggleInternshipVisibility();
                case 4 -> viewApplications();
                case 5 -> approveRejectApplications();
                case 0 -> {
                    // Logout handled by the main program logic after this method returns
                    System.out.println("Logging out...");
                }
            }
        }
    }

    private void viewMyInternships() {
        List<Internship> myInternships = internshipManager.getInternshipsByRep(currentUser.getID());
        System.out.println("\n--- My Internship Listings ---");
        if (myInternships.isEmpty()) {
            System.out.println("You have no active internship listings.");
        } else {
            System.out.printf("%-10s %-30s %-10s %-12s %-12s%n", "ID", "Title", "Level", "Status", "Visible");
            System.out.println("-".repeat(75));
            for (Internship i : myInternships) {
                 System.out.printf("%-10s %-30s %-10s %-12s %-12s%n", 
                    i.getInternshipID(), i.getTitle(), i.getLevel(), i.getStatus(), i.isVisible() ? "Yes" : "No");
            }
        }
        inputString("Press Enter to continue...");
    }

    private void createNewInternship() {
        System.out.println("\n--- Create New Internship Listing ---");
        String title = inputString("Enter Title: ");
        String description = inputString("Enter Description: ");
        String major = inputString("Enter Preferred Major (e.g., Computer Science): ");
        int slots = inputInteger("Enter Total Slots (1-10): ", 1, 10);

        System.out.println("Select Internship Level:");
        System.out.println("1. BASIC\n2. INTERMEDIATE\n3. ADVANCED");
        int levelChoice = inputInteger("Enter Level (1-3): ", 1, 3);
        InternshipLevel level = switch (levelChoice) {
            case 1 -> InternshipLevel.BASIC;
            case 2 -> InternshipLevel.INTERMEDIATE;
            case 3 -> InternshipLevel.ADVANCED;
            default -> null; 
        };

        LocalDate openDate = inputDate("Enter Opening Date");
        LocalDate closeDate = inputDate("Enter Closing Date");

        if (level != null) {
            internshipManager.createListing(
                currentUser.getID(), title, description, level, major, 
                openDate, closeDate, currentUser.getCompanyName(), slots
            );
        } else {
            System.out.println("Failed to create listing due to invalid input.");
        }
        inputString("Press Enter to continue...");
    }

    private void toggleInternshipVisibility() {
        String id = inputString("Enter Internship ID to toggle visibility: ");
        internshipManager.toggleVisibility(id, currentUser.getID());
        inputString("Press Enter to continue...");
    }

    private void viewApplications() {
        List<Application> allApplications = applicationManager.getApplicationList().stream()
            .filter(app -> app.getInternship() != null && app.getInternship().getCompanyRepId().equals(currentUser.getID()))
            .toList();
        List<Internship> myInternships = internshipManager.getInternshipsByRep(currentUser.getID());
        System.out.println("\n--- Applications for My Internships ---");
        if (myInternships.isEmpty()) {
            System.out.println("You have no internships to view applications for.");
            inputString("Press Enter to continue...");
            return;
        }

        // Get all applications associated with this company representative's listings
        
        if (allApplications.isEmpty()) {
            System.out.println("No applications found for your listings.");
            inputString("Press Enter to continue...");
            return;
        }

        System.out.printf("%-12s %-12s %-30s %-15s%n", "AppID", "StudentID", "Internship Title", "Status");
        System.out.println("-".repeat(70));
        for (Application a : allApplications) {
            String title = a.getInternship().getTitle();
            System.out.printf("%-12s %-12s %-30s %-15s%n", 
                a.getID(), a.getStudentID(), title, a.getStatus());
        }
        inputString("Press Enter to continue...");
    }

    private void approveRejectApplications() {
        System.out.println("\n--- Approve/Reject Applications ---");
        String appId = inputString("Enter Application ID to process: ");
        Application app = applicationManager.findByID(appId);

        if (app == null) {
            System.out.println("Application not found.");
            return;
        }
        
        if (app.getInternship() == null || !app.getInternship().getCompanyRepId().equals(currentUser.getID())) {
            System.out.println("Error: This application is not associated with your company's listings.");
            return;
        }

        if (app.getStatus() != Application.ApplicationStatus.PENDING) {
            System.out.println("Error: Only PENDING applications can be actioned. Current status: " + app.getStatus());
            return;
        }

        System.out.println("Current Application: " + app.getID() + " for " + app.getInternship().getTitle() + " (Student: " + app.getStudentID() + ")");
        System.out.println("1. Approve\n2. Reject");
        int action = inputInteger("Choose action: ", 1, 2);

        if (action == 1) {
            currentUser.approveApplication(app);
            System.out.println("Application Approved. Student informed.");
        } else if (action == 2) {
            currentUser.rejectApplication(app);
            System.out.println("Application Rejected. Student informed.");
        }
        inputString("Press Enter to continue...");
    }
}
