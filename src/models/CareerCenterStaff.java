package models;

import controllers.ApplicationManager;
import controllers.FileHandler;
import controllers.InternshipManager;
import controllers.UserManager;

/**
 * <b>CAREER CENTER STAFF CLASS</b><br>
 * Represents a Career Center Staff user in the internship management system. Registration is automatic by reading in from the staff list csv file.
 * <br><br>
 * Responsibilities include:
 * <ul>
 * <li>Authorizing or rejecting Company Representative account creation
 * </ul>
 * 
 * Inherits basic functions like login, logout, and change password from {@link models.User}.
 * 
 * @author Hanyue
 */
public class CareerCenterStaff extends User {

    private String department;
    private String role;
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
    public CareerCenterStaff(String ID, String name, String role, String department,String email) {
        super(ID, name, email);
        this.role = role;
        this.userType = TypesOfUser.CareerCenterStaff;
        this.department = department;
    }

    public String getDepartment() {return this.department;}

    public void setDepartment(String department) {this.department = department;}

    public String getRole() {return this.role;}

    public void setRole(String role) {this.role = role;}

    /**
     * Approves or rejects the registration of a Company Representative.
     * @param companyRepID The ID of the company representative to review.
     * @param approve true to approve, false to reject.
     */
    public void authorizeCompanyAccount(String companyRepID, boolean approve) {
        if (approve) {
            boolean approvalStatus = userManager.approveCompanyRepresentative(companyRepID);
            if(approvalStatus) {
            	System.out.println("Company representative " + companyRepID + " approved.");
            }
        } else {
        	boolean rejectStatus = userManager.rejectCompanyRepresentative(companyRepID);
        	if(rejectStatus) {
        		 System.out.println("Company representative " + companyRepID + " rejected.");
        	}
        }
    }
    
}
