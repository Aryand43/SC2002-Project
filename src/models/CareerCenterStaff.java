package models;

import controllers.FileHandler;
import managers.UserManager;
import managers.InternshipManager;
import managers.ApplicationManager;
import java.util.List;

/**
 * <b>CAREER CENTER STAFF CLASS</b><br>
 * Represents a Career Center Staff user in the internship management system. Registration is automatic by reading in from the staff list csv file.
 * <br><br>
 * Responsibilities include:
 * <ul>
 * <li>Authorizing or rejecting Company Representative account creation</li>
 * <li>Approving or rejecting internship opportunities</li>
 * <li>Approving or rejecting student withdrawal requests (able to do it both before and after internship placement confirmation)</li>
 * <li>Generating comprehensive internship reports with filters</li>
 * </ul>
 * 
 * Inherits basic functions like login, logout, and change password from {@link models.User}.
 * 
 * @author Hanyue
 */
public class CareerCenterStaff extends User {

    private String department;
    private FileHandler fileHandler;
    private UserManager userManager;
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;

    /**
     * Class Constructor
     * @param ID Staff ID
     * @param name Staff name
     * @param email Staff email
     * @param department Staff department (e.g. Career Services Office)
     */
    public CareerCenterStaff(String ID, String name, String email, String department) {
        super(ID, name, email);
        this.userType = TypesOfUser.CareerCenterStaff;
        this.department = department;
    }

    public String getDepartment() {return this.department;}

    public void setDepartment(String department) {this.department = department;}

    /**
     * Approves or rejects the registration of a Company Representative.
     * @param companyRepID The ID of the company representative to review.
     * @param approve true to approve, false to reject.
     */
    public void authorizeCompanyAccount(String companyRepID, boolean approve) {
        if (approve) {
            userManager.approveCompanyRepresentative(companyRepID);
            System.out.println("Company representative " + companyRepID + " approved.");
        } else {
            userManager.rejectCompanyRepresentative(companyRepID);
            System.out.println("Company representative " + companyRepID + " rejected.");
        }
    }

    /**
     * Approves or rejects internship opportunities submitted by Company Representatives.
     * @param internshipID The internship ID to review.
     * @param approve true to approve, false to reject.
     */
    public void reviewInternshipOpportunity(String internshipID, boolean approve) {
        if (approve) {
            internshipManager.updateInternshipStatus(internshipID, "Approved");
            System.out.println("Internship " + internshipID + " approved.");
        } else {
            internshipManager.updateInternshipStatus(internshipID, "Rejected");
            System.out.println("Internship " + internshipID + " rejected.");
        }
    }

    /**
     * Approves or rejects a student's withdrawal request (able to do this both before and after placement confirmation).
     * @param applicationID The application ID related to the withdrawal.
     * @param approve true to approve, false to reject.
     */
    public void reviewStudentWithdrawal(String applicationID, boolean approve) {
        if (approve) {
            applicationManager.approveStudentWithdrawal(applicationID);
            System.out.println("Withdrawal " + applicationID + " approved.");
        } else {
            applicationManager.rejectStudentWithdrawal(applicationID);
            System.out.println("Withdrawal " + applicationID + " rejected.");
        }
    }

    /**
     * Generates internship reports with optional filters.
     * @param status Filter by internship status (e.g. "Approved", "Pending")
     * @param preferredMajor Filter by preferred major
     * @param internshipLevel Filter by internship level (e.g. "Undergraduate", "Postgraduate")
     */
    public void generateInternshipReport(String status, String preferredMajor, String internshipLevel) {
        List<?> filteredList = internshipManager.filterInternships(status, preferredMajor, internshipLevel);
        System.out.println("\n=== Internship Report ===");
        for (Object internship : filteredList) {
            System.out.println(internship.toString());
        }
    }
}