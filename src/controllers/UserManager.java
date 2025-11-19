package controllers;

import java.util.ArrayList;
import models.CareerCenterStaff;
import models.CompanyRepresentative;
import models.Student;
import models.User;

/**
 * The User Manager will be in charge of logging in and logging out. 
 * <br>User Manager will also be used in authorizing the company representative only if a staff account is used.<br>
 * @author Shayne Low
 */
public class UserManager {
    /**
     * studentList stores an array of initialised Student objects by FileHandler
     */
    private ArrayList<Student> studentList;
    /**
     * repList stores an array of initialised Company Representative objects by FileHandler
     */
    private ArrayList<CompanyRepresentative> repList;
    /**
     * staffList stores an array of initialised Career Center Staff objects by FileHandler
     */
    private ArrayList<CareerCenterStaff> staffList; 

    /**
     * userList stores an array of all types of User objects (Student, Company Representative, Career Center Staff)
     */
    private ArrayList<User> userList;
    
    /**
     * Stores the whole User object
     */
    private User currentUser;

    /**
     * Class Constructor<br>
     * Ensure each object list is loaded accordingly eg. Student list from main code is loaded into Student list in User Manager.
     */
    public UserManager(){
        // Initializing lists into fields  
        StaffSerializer staffSerializer = new StaffSerializer();
    	FileHandler<CareerCenterStaff> staffFileHandler = new FileHandler<>(staffSerializer);
    	staffList = staffFileHandler.readFromFile();

    	StudentSerializer studentSerializer = new StudentSerializer();
    	FileHandler<Student> studentFileHandler = new FileHandler<>(studentSerializer);
    	studentList = studentFileHandler.readFromFile();

    	CompanyRepresentativeSerializer companyRepSerializer = new CompanyRepresentativeSerializer();
    	FileHandler<CompanyRepresentative> companyFileHandler = new FileHandler<>(companyRepSerializer);
    	repList = companyFileHandler.readFromFile();
        
        userList = getRespectiveUserList();
    }
    
    /**
     * Getter Function
     * @return Just the current User object
     */
    public User getCurrentUser(){return this.currentUser;}

    /**
     * Setter Function
     * @return When user logs out, clear the current user object in User Manager
     */
    public void clearCurrentUser(){this.currentUser = null;}

    /**
     * Method to get list of user type at login stage, where there is no current user in the program.<br>
     * User's ID decides which list is used. 
     * <ol>
     * <li>If the ID inputted by the user starts with "U"/"u", user is a student</li>
     * <li>If the ID inputted by the user starts with "CR"/"cr", user is a company representative</li>
     * <li>Else, user is a staff</li>
     * </ol>
     * @return The respective list of objects associated with the ID.
     */
    // public ArrayList<User> getRespectiveUserList(String ID){   
    //     if (Character.toString(ID.charAt(0)).equalsIgnoreCase("U")){
    //         return this.studentList;
    //     }
    //     else if (Character.toString(ID.charAt(0)).equalsIgnoreCase("CR")){
    //         return this.repList;
    //     }
    //     else{
    //        return this.staffList;
    //     }
    // }

    /**
     * Login function checks the inputted ID, password and login state.<br>
     * If both ID and password were entered correctly, and login state is false, the user will sucessfully login.<br>
     * Else, user will be unable to login.
     * 
     */
    public boolean login(String ID, String password, java.util.Scanner sc) {

        for (User u : userList) {

            // If user is NOT a company rep, use ID for login
            if (u.getUserType() != User.TypesOfUser.CompanyRepresentative) {

                if (u.getID().equalsIgnoreCase(ID)) {

                    if (u.getPassword().equals(password) && !u.isLoggedIn()) {
                        u.setLogin(true);
                        this.currentUser = u;

                        if (currentUser.isPasswordChanged()) {
                            System.out.println("Login Successful! Welcome " + currentUser.getUserName() + "!");
                        } else {
                            changePassword(currentUser.getID(), password, sc);
                            u.setLogin(false);
                            System.out.println("Password Reset! Please login again!");
                            return false;
                        }
                        return true;
                    }

                    else if (!u.getPassword().equals(password)) {
                        System.out.println("Wrong Password!");
                        return false;
                    }
                }
            }

            // Company representative login using email
            else {
                if (u.getEmail().equals(ID)) {

                    if (u.getPassword().equals(password) && !u.isLoggedIn()) {
                        u.setLogin(true);
                        this.currentUser = u;
                        
                        if (currentUser.isPasswordChanged()) {
                            System.out.println("Login Successful! Welcome " + currentUser.getUserName() + "!");
                        } else {
                            changePassword(currentUser.getID(), password);
                            u.setLogin(false);
                            System.out.println("Password Reset! Please login again!");
                            return false;
                        }
                        return true;
                    }

                    else if (!u.getPassword().equals(password)) {
                        System.out.println("Wrong Password!");
                        return false;
                    }
                }
            }
        }

        System.out.println("Login Failed! No Such User!");
        return false;
    }

    /**
     * Function that combines Student, Staff, Company Representative into one userlist by upcasting them provided if their account is approved
     * @return ArrayList of User objects.
     */
    public ArrayList<User> getRespectiveUserList(){
    	ArrayList<User> userList = new ArrayList<>();
        for(CareerCenterStaff staff: staffList) {
            User u = staff;
            userList.add(u);
        }
    	
    	for(Student s: studentList) {
    		User u = s;
    		userList.add(u);
    	}
    	
    	for(CompanyRepresentative cr: repList) {
    		if(cr.isApproved() != null && cr.isApproved() == true) {
    		User u = cr;
    		userList.add(u);
        }
    	}
    	
    	
    	return userList;
    }
    
    /**
     * Logout function sets the current user's login state to false, and then clears the current user from User Manager.<br> 
     * System message displayed in program to let user know they have logged out.
     */
    public void logout(){
        this.getCurrentUser().setLogin(false);
        System.out.printf("%s Successfully logged out", this.getCurrentUser().getUserName());
        this.clearCurrentUser();
    }
    
    /**
     * Function that approves Company Representative's application for account by getting their ID and returns the a status to indicate if the approval was a success.
     * @param String repID
     * @return Boolean
     */
    public boolean approveCompanyRepresentative(String repID){
    	for(CompanyRepresentative cr: repList) {
    		if(cr.getID().equals(repID)) {
    			if(cr.isApproved() == null) {
    				cr.setApproved(true);
    				User u = cr;
    				userList.add(u);
    				return true;
    			}
    			else if(cr.isApproved()) {
    				System.out.println("Error, Cant Accept, CompanyRepresentative Has Already Been Approved!");
    				return false;
    			}
    			else {
    				System.out.println("Error! Cant Accept, CompanyRepresentative Has Already Been Rejected!");
    				return false;
    			}
    		}
    	}
    	System.out.println("Error: Company Representative with ID '" + repID + "' not found.");
        return false;
    }
    
    /**
     * Function that rejects Company Representative's application for account by getting their ID and returns the a status to indicate if the rejection was a success.
     * @param String repID
     * @return Boolean
     */
    public boolean rejectCompanyRepresentative(String repID){
    	for(CompanyRepresentative cr: repList) {
    		if(cr.getID().equals(repID)) {
    			if(cr.isApproved() == null) { 
    				cr.setApproved(false);
    				return true;
    			}
    			else if(cr.isApproved()) {
    				System.out.println("Error! Cant Reject, CompanyRepresentative Has Already Been Approved!");
    				return false;
    			}
    			else {
    				System.out.println("Error! Cant Reject, CompanyRepresentative Has Already Been Rejected!");
    				return false;
    			}
    		}
    	}
    	System.out.println("Error: Company Representative with ID '" + repID + "' not found.");
        return false;
    }
    
    /**
     * Function that resets the password of User to the default password when they forget their password.
     * @param userID
     * @param userEmail
     */
    public void resetPassword(String userID, String userEmail) {
    	User resetUser = userList.stream().filter(s -> s.getID().equals(userID)).filter(s -> s.getEmail().equals(userEmail)).findFirst().orElse(null);
    	if (resetUser == null) {
    		System.out.println("No Such User/Mismatched UserId and User Email!");
    	}
    	else {
    		resetUser.resetDefaultPassword();
    		System.out.println("Password Sucessfully Resetted! New Password: password ");
    	}
    }
    
    /**
     * Function that enables user to change the password by asking user for their input of oldPassword and their ID.
     * @param userID
     * @param oldPassword
     */
    public void changePassword(String userID, String oldPassword, java.util.Scanner sc){
     User u = userList.stream().filter(s -> s.getID().equals(userID)).filter(s -> s.getPassword().equals(oldPassword)).findFirst().orElse(null);
     if(u == null) {
      System.out.println("No Such User/Mismatched UserId and oldPassword");
     }
     else {
      u.setPassword(oldPassword, sc);
            System.out.println("Login again with new password.");
     }
    }

    // Backwards-compatible wrapper that uses System.in Scanner
    public void changePassword(String userID, String oldPassword){
        java.util.Scanner sc = new java.util.Scanner(System.in);
        changePassword(userID, oldPassword, sc);
    }
    
    /**
     * Function that adds Companyrepresentative to the ArrayList of CompanyRepresentative
     * @param cr
     */
    public void addCompanyRepresentative(CompanyRepresentative cr) {
    	repList.add(cr);
    }

    /**
     * Return list of students (concrete Student objects) stored in manager
     */
    public ArrayList<Student> getStudentList(){
        return this.studentList;
    }

    /**
     * Return list of company representatives (concrete CompanyRepresentative objects)
     */
    public ArrayList<CompanyRepresentative> getRepList(){
    	return this.repList;
    }

    /**
     * Returns a Student object by its ID.
     * @param studentID The ID of the student to retrieve.
     * @return The Student object if found, otherwise null.
     */
    public User getStudentByID(String studentID) {
        for (User s : userList) {
            if (s.getID().equals(studentID)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Return list of career center staff (concrete CareerCenterStaff objects)
     */
    public ArrayList<CareerCenterStaff> getStaffList(){
    	return this.staffList;
    }
    
    /**
     * Function that saves the List of Students, Staff, Company Representative back into the CSV file.
     */
    public void saveToFile() {
        StaffSerializer staffSerializer = new StaffSerializer();
    	FileHandler<CareerCenterStaff> staffFileHandler = new FileHandler<>(staffSerializer);
    	staffFileHandler.writeToFile(this.staffList);

    	StudentSerializer studentSerializer = new StudentSerializer();
    	FileHandler<Student> studentFileHandler = new FileHandler<>(studentSerializer);
    	studentFileHandler.writeToFile(this.studentList);

    	CompanyRepresentativeSerializer companyRepSerializer = new CompanyRepresentativeSerializer();
    	FileHandler<CompanyRepresentative> companyFileHandler = new FileHandler<>(companyRepSerializer);
    	companyFileHandler.writeToFile(this.repList);
    	System.out.println("Saving to Files");
    }
}
