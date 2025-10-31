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
}
