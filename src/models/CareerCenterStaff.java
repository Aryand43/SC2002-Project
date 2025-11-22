package models;

/**
 * Represents a Career Center Staff user in the internship management system. 
 * Registration is automatic by reading in from the staff list CSV file.
 * Inherits basic functions like login, logout, and change password from {@link models.User}.
 * <br><br>
 * <b>Responsibilities:</b>
 * <ul>
 * <li><b>Company Representative Account Management</b> - Implemented in {@link controllers.UserManager}
 *     <ul>
 *     <li>View/Approve/Reject pending Company Representative accounts</li>
 *     </ul>
 * </li>
 * <li><b>Internship Opportunity Management</b> - Implemented in {@link controllers.InternshipManager}
 *     <ul>
 *     <li>View/Approve/Reject pending internship postings</li>
 *     </ul>
 * </li>
 * <li><b>Student Withdrawal Management</b> - Implemented in {@link controllers.ApplicationManager}
 *     <ul>
 *     <li>View/Approve/Reject student withdrawal requests</li>
 *     </ul>
 * </li>
 * <li><b>Report Generation</b> - Implemented in {@link controllers.ReportGenerator}
 * </li>
 * </ul>
 * <br>
 * 
 * @author Hanyue
 */
public class CareerCenterStaff extends User {

    private String department;
    private String role;

    /**
     * Constructor
     * @param ID Sets the staff ID
     * @param name Sets the staff name
     * @param role Sets the staff role
     * @param department Sets the staff department
     * @param email Sets the staff email
     * @param password User's password<br><br>
     */
    public CareerCenterStaff(String ID, String name, String role, String department,String email, String password) {
        super(ID, name, email, password);
        this.role = role;
        this.userType = TypesOfUser.CareerCenterStaff;
        this.department = department;
    }

    /**
     * Getter Function
     * @return Department
     */
    public String getDepartment() {return this.department;}

    /**
     * Setter Function
     * Sets the department of the Career Center Staff
     * @param department The department to set
     */
    public void setDepartment(String department) {this.department = department;}

    /**
     * Getter Function
     * @return Role
     */
    public String getRole() {return this.role;}

    /**
     * Setter Function
     * Sets the role of the Career Center Staff
     * @param role The role to set
     */
    public void setRole(String role) {this.role = role;}
}

