package models;

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

    /**
     * Class Constructor
     * @param ID Sets the staff ID
     * @param name Sets the staff name
     * @param role Sets the staff role
     * @param department Sets the staff department (e.g. Career Services Office)
     * @param email Sets the staff email<br><br>
     * User type is automatically set to CareerCenterStaff.
     */
    public CareerCenterStaff(String ID, String name, String role, String department,String email) {
        super(ID, name, email);
        this.role = role;
        this.userType = TypesOfUser.CareerCenterStaff;
        this.department = department;
    }
    
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

    /**
     * Career Center Staff can approve or reject Company Representative account creation.<br>
     * This functionality is implemented in {@link controllers.UserManager}.
     */
}
