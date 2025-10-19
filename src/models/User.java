package models;
import java.util.Scanner;

/**
 * <b>USER CLASS</b><br>
 * A User object will contain
 * <ol>   
 * <li>User ID</li>
 * <li>Name</li>
 * <li>Password</li>
 * <li>Email</li>
 * <li>Type of User</li>
 * </ol><br>
 * A User object can be one of the types below:
 * <ol>
 * <li>User</li>
 * <li>Student</li>
 * <li>Career Center Staff</li>
 * <li>Company Representative</li>
 * </ol>
 * 
 * Other than the default type (User), every other User type will also include their own specific fields.<br>
 * <br>ALL Users will have access to basic user management features, including login, logout and change password.
 * @author Shayne Low
 */
public class User {
    /**
    * Enum that contains different user account types - User, Student, CareerCenterStaff, CompanyRepresentative<br>
    * User objects of type "Student" and "Career Center Staff" are automatically assigned their respective types from the sample excel file.<br><br>
    * The default type "User" is initiated first, as in the case of "Company Representative" they must be approved by a user<br>
    * that is a "Career Center Staffs" type first, before officially becoming a "Company Representative".
    * <br><br>
     */
    public enum TypesOfUser {User, Student, CareerCenterStaff, CompanyRepresentative};
    protected final String ID;
    protected final String name;
    protected final String email;
    protected String password;
    protected TypesOfUser userType;

     /**
     * Class Constructor
     * @param ID Sets the user ID.
     * @param name Sets the user name.<br><br>
     * For new User objects, set user's password to default: "password".
     */
    public User(String ID, String name, String email){
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = "password";
        this.userType = TypesOfUser.User;
    }

    /**
     * Getter Function
     * @return ID
     */
    public String getID(){return this.ID;}
    /**
     * Getter Function
     * @return Name
     */
    public String getUserName(){return this.name;}
    /**
     * Getter Function
     * @return Email
     */
    public String getEmail(){return this.email;}
    /**
     * Getter Function
     * @return Password
     */
    public String getPassword(){return this.password;}

    /**
     * Set/Change user's password <br>
     * @param oldPass Takes in user's old password to enable password change.
     * <br><br>
     * Checks if user's new password has:
     * <ul>
     * <li>At least 8 characters</li>
     * <li>At least 1 Uppercase letter</li>
     * <li>At least 1 Digit</li>
     * <li>At least 1 Special Character</li>
     * </ul>
     * @return 
     * <ol>
     * <li>true if password successfully set </li>
     * <li>false if user wants to exit or enters invalid password. </li>
     */
    public boolean setPassword(String oldPass){
        Scanner sc = new Scanner(System.in);
        while(true){
            if (oldPass.compareTo(this.password) == 0){
                System.out.print("\nEnter new password (Blank password to exit): ");
                String newPass = sc.nextLine();
                if (isValidPassword(newPass)) {
                    this.password = newPass;
                    return true;
                }
                else if(newPass.compareTo("") == 0){
                    return false;
                }
                else {
                    System.out.println("- At least 8 characters");
                    System.out.println("- At least 1 uppercase letter");
                    System.out.println("- At least 1 digit");
                    System.out.println("- At least 1 special character");
                }
            }   
            else {
                System.out.println("\nWrong Password Entered!");
                return false;
            }
            sc.close();
        }
    }
    
    /**
     * Helper function for setPassword() to validate new password entered.
     * @param pasword Takes in user's old password to enable password change.
     * <br><br>
     * @return 
     * <ol> 
     * <li>true with system message: "Valid new password"</li>
     * <li>false with system message: "Invalid new password" and returns false</li>
     * </ol>
     */
    public static boolean isValidPassword(String password) {
        boolean length = password.length() >= 8; // Password length is at least 8
        boolean upper = password.chars().anyMatch(Character::isUpperCase); // At least 1 upper
        boolean digit = password.chars().anyMatch(Character::isDigit); // At least 1 number
        // Regex to check if there is at least 1 special character
        boolean special = password.matches(".*[!@#$%^&*()_+\\-={};':\"\\\\|,.<>/?].*");

        // ALL conditions met before able to change password
        if (length && upper && digit && special) {
            System.out.println("Valid new password.");
            return true;
        } 
        
        else {
            System.out.println("Invalid new password.");
            return false;
        }
    }
                

}


