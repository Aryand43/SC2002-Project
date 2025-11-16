package models;

import java.util.ArrayList;

/**
 * Represents a Student user in the Internship Management System.
 * <p>
 * A student can:
* <ul>
* <li>Apply for internships (max 3 active applications)</li>
* <li>Accept one internship offer</li>
* <li>Request to withdraw applications</li>
* <li>View all submitted applications</li>
* </ul>
* </p>
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
 * This class extends {@link models.User}.
 * @author Joel Lee
 */
public class Student extends User {
    
    private int yearOfStudy;                // Year 1–4
    private String major;                   // CSC, EEE, MAE, etc.
    private ArrayList<Application> applications; // Up to 3 active applications

    /**
     * Constructor for Student.
     * @param ID Student ID
     * @param name Student name
     * @param email Student email
     * @param yearOfStudy 1–4
     * @param major Student’s major (e.g., CSC, EEE)
     */
    public Student(String ID, String name, String major, int yearOfStudy, String email) {
        super(ID, name, email);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
        this.userType = TypesOfUser.Student;
    }
    
    public Student(String ID, String name, String major, int yearOfStudy, String email, String password) {
        super(ID, name, email, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
        this.userType = TypesOfUser.Student;
    }

    // -----------------------------
    // Getters and Setters
    // -----------------------------
    public int getYearOfStudy() { return yearOfStudy; }
    public String getMajor() { return major; }
    public ArrayList<Application> getApplications() { return applications; }
    public int noOfApplications() { return applications.size(); }


    // -----------------------------
    // Internship Management
    // -----------------------------

    /**
     * Apply for a new internship opportunity.
     * @param application Application object
     * @return true if application successful, false otherwise
     */
    public boolean applyForInternship(Application application) {
        if (applications.size() >= 3) {
            System.out.println("You can only apply for a maximum of 3 internship opportunities at once.");
            return false;
        }

        Internship internship = application.getInternship();
        if (internship == null) {
            System.out.println("Invalid application: internship not set.");
            return false;
        }

        // Check internship is open for applications
        if (!internship.isOpenForApplications()) {
            System.out.println("This internship is not open for applications.");
            return false;
        }

        //Checking the restriction for Year 1-2 students
        if (yearOfStudy <= 2 && internship.getLevel() != Internship.InternshipLevel.BASIC) {
            System.out.println("Year 1 and 2 students can only apply for Basic-level internships.");
            return false;
        }

        // Add application
        applications.add(application);
        System.out.println("Application submitted: " + internship.getTitle());
        return true;
    }   

    /**
     * Accept an internship offer if status is "Successful".
     * Automatically withdraws all other applications.
     */
    public void acceptInternship(String internshipID) {
        Application accepted = null;
        for (Application app : applications) {
            if (app.getInternship() != null && app.getInternship().getInternshipID().equals(internshipID) &&
                app.getStatus() == Application.ApplicationStatus.SUCCESSFUL) {
                accepted = app;
                break;
            }
        }

        if (accepted == null) {
            System.out.println("No successful internship found with the given ID.");
            return;
        }

        // Withdraw other applications
        for (Application app : applications) {
            if (app.getInternship() == null) continue;
            if (!app.getInternship().getInternshipID().equals(internshipID)) {
                app.setStatus(Application.ApplicationStatus.WITHDRAWN);
            }
        }

        // Update confirmed slots on the accepted internship
        accepted.getInternship().incrementConfirmedSlots();

        System.out.printf("Internship '%s' accepted. All other applications withdrawn.%n", accepted.getInternship().getTitle());
    }

    /**
     * Request to withdraw an internship application.
     * @param internshipID ID of internship to withdraw
     */
    public void requestWithdrawal(String internshipID) {
        for (Application app : applications) {
            if (app.getInternship() != null && app.getInternship().getInternshipID().equals(internshipID)) {
                app.setStatus(Application.ApplicationStatus.WITHDRAW_REQUESTED);
                System.out.printf("Withdrawal requested for internship '%s' (Subject to Career Center approval).%n", app.getInternship().getTitle());
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
        for (Application app : applications) {
            String title = app.getInternship() == null ? "<unknown>" : app.getInternship().getTitle();
            String level = app.getInternship() == null ? "<unknown>" : app.getInternship().getLevel().toString();
            System.out.printf("- %s (%s): %s%n", title, level, app.getStatus());
        }
    }

}



