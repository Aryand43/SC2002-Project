package models;

import java.util.ArrayList;

/**
 * <b>STUDENT CLASS</b><br>
 * Extends {@link models.User} and defines specific behavior and permissions for student users.
 * <br>
 * A Student object will contain:
 * <ul>
 *   <li>Year of Study (1–4)</li>
 *   <li>Major (CSC, EEE, MAE, etc.)</li>
 *   <li>Visibility status (true/false)</li>
 *   <li>List of Internship Applications (max 3)</li>
 * </ul>
 * 
 * <br>Based on system requirements:
 * <ul>
 *   <li>Year 1–2: can only apply for Basic-level internships</li>
 *   <li>Year 3–4: can apply for any level (Basic, Intermediate, Advanced)</li>
 *   <li>Only 1 internship can be accepted</li>
 *   <li>Student can withdraw an application (subject to approval)</li>
 * </ul>
 * 
 * @author Joel Lee
 */
public class Student extends User {
    
    private int yearOfStudy;                // Year 1–4
    private String major;                   // CSC, EEE, MAE, etc.
    private ArrayList<InternshipApplication> applications; // Up to 3 active applications
    private int currentApplications;

    /**
     * Constructor for Student.
     * @param ID Student ID
     * @param name Student name
     * @param email Student email
     * @param yearOfStudy 1–4
     * @param major Student’s major (e.g., CSC, EEE)
     */
    public Student(String ID, String name, String email, int yearOfStudy, String major) {
        super(ID, name, email);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
        this.userType = TypesOfUser.Student;
        this.currentApplications = 0;
    }

    // -----------------------------
    // Getters and Setters
    // -----------------------------
    public int getYearOfStudy() { return yearOfStudy; }
    public String getMajor() { return major; }
    public ArrayList<InternshipApplication> getApplications() { return applications; }


    // -----------------------------
    // Internship Management
    // -----------------------------

    /**
     * Apply for a new internship opportunity.
     * @param internship InternshipApplication object
     * @return true if application successful, false otherwise
     */
    public boolean applyForInternship(InternshipApplication internship) {
        if (applications.size() >= 3) {
            System.out.println("You can only apply for a maximum of 3 internship opportunities at once.");
            return false;
        }

        if (yearOfStudy <= 2 && !internship.getLevel().equalsIgnoreCase("Basic")) {
            System.out.println("Year 1 and 2 students can only apply for Basic-level internships.");
            return false;
        }

        // Add application
        applications.add(internship);
        System.out.printf("Internship '%s' applied successfully. Status: Pending.%n", internship.getTitle());
        return true;
    }

    /**
     * Accept an internship offer if status is "Successful".
     * Automatically withdraws all other applications.
     */
    public void acceptInternship(String internshipID) {
        InternshipApplication accepted = null;
        for (InternshipApplication app : applications) {
            if (app.getID().equals(internshipID) && app.getStatus().equalsIgnoreCase("Successful")) {
                accepted = app;
                break;
            }
        }

        if (accepted == null) {
            System.out.println("No successful internship found with the given ID.");
            return;
        }

        // Withdraw other applications
        for (InternshipApplication app : applications) {
            if (!app.getID().equals(internshipID)) {
                app.setStatus("Withdrawn");
            }
        }

        System.out.printf("Internship '%s' accepted. All other applications withdrawn.%n", accepted.getTitle());
    }

    /**
     * Request to withdraw an internship application.
     * @param internshipID ID of internship to withdraw
     */
    public void requestWithdrawal(String internshipID) {
        for (InternshipApplication app : applications) {
            if (app.getID().equals(internshipID)) {
                app.setStatus("Withdraw Requested");
                System.out.printf("Withdrawal requested for internship '%s' (Subject to Career Center approval).%n", app.getTitle());
                return;
            }
        }
        System.out.println("No internship found with the given ID.");
    }

    /**
     * View applications and their statuses (Pending, Successful, Unsuccessful, Withdrawn)
     */
    public void viewApplications() {
        System.out.println("Your Internship Applications:");
        for (InternshipApplication app : applications) {
            System.out.printf("- %s (%s): %s%n", app.getTitle(), app.getLevel(), app.getStatus());
        }
    }

}

