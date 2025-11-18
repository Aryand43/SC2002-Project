package models;

import java.util.ArrayList;

/**
 * Represents a Student user in the Internship Management System.
 * <p>
 * A student holds and manages a collection of their internship applications.
 * The Student class focuses on student-specific data and validation rules:
 * <ul>
 *   <li>Year of Study (1–4): determines which internship levels can be accessed</li>
 *   <li>Major (CSC, EEE, MAE, etc.): for categorization and filtering</li>
 *   <li>Application History: collection of submitted applications</li>
 * </ul>
 * </p>
 * <p>
 * Application Restrictions:
 * <ul>
 *   <li>Year 1–2: can only apply for Basic-level internships</li>
 *   <li>Year 3: can apply for any level (Basic, Intermediate, Advanced)</li>
 *   <li>Maximum 3 active applications at any time</li>
 *   <li>Only 1 internship offer can be accepted (managed by ApplicationManager)</li>
 * </ul>
 * </p>
 * <p>
 * </p>
 * 
 * This class extends {@link models.User}.
 * @author Joel Lee
 */
public class Student extends User {
    
    private final int yearOfStudy;                // Year 1–4
    private final String major;                   // CSC, EEE, MAE, etc.
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
    
    /**
     * Convenient Constructor for the Serializer that takes in extra parameter of password
     * @param ID Student ID
     * @param name Student name
     * @param major Student Major
     * @param yearOfStudy 1–4
     * @param email Student email
     * @param password Student's password that is remembered by the system
     */
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
    // Internship Application Validation
    // -----------------------------

    /**
     * Validates if a student can apply for an internship.
     * Checks:
     * 1. Application limit (max 3 active applications)
     * 2. Year of Study restriction (Years 1–2 can only apply for BASIC level)
     * 3. Internship validity
     * 
     * This method is for validation only. The actual application creation
     * and persistence is handled by ApplicationManager.apply().
     * 
     * @param internship The internship student is trying to apply for
     * @return true if student can apply, false otherwise
     */
    public boolean canApply(Internship internship) {
        // Check if student has reached the 3 application limit
        if (applications.size() >= 3) {
            System.out.println("You can only apply for a maximum of 3 internship opportunities at once.");
            return false;
        }

        // Check if internship is valid
        if (internship == null) {
            System.out.println("Invalid internship listing");
            return false;
        }

         // Check visibility of internship
        if (!internship.isVisible()) {
        System.out.println("This internship is not currently visible and cannot be applied for.");
        return false;
        }

        //Checking the restriction for Year 1-2 students
        if (yearOfStudy <= 2 && internship.getLevel() != Internship.InternshipLevel.BASIC) {
            System.out.println("Year 1 and 2 students can only apply for Basic-level internships.");
            return false;
        }

        // Major preference restriction
        if (internship.getPreferredMajor() != null 
            && !internship.getPreferredMajor().isEmpty()
            && !internship.getPreferredMajor().contains(this.major)) {
        System.out.println("Your major (" + this.major + 
                           ") does not match the required major preference for this internship.");
        return false;
        }

        //Check if already applied to that internship
        for (Application a : this.getApplications()){
            if(a.getInternship().getInternshipID() == internship.getInternshipID()){
                System.out.print("You have already applied for this internship!");
                return false;
            }
        }  
        return true;
    }

    /**
     * Adds an application to the student's application list.
     * (Called by ApplicationManager after successful persistence)
     * @param application The application to add
     */
    public void addApplication(Application application) {
        if (application != null && !applications.contains(application)) {
            applications.add(application);
        }
    }

}



