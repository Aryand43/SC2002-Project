package controllers;

import java.util.ArrayList;
import models.CareerCenterStaff;
import models.CompanyRepresentative;
import models.Student;
import models.User;

/**
 * <b>USER MANAGER CLASS</b><br>
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
    public boolean login(String ID, String password){

         for(User u: userList){
            // If user is company rep, have to check email for login detials
            if (u.getUserType() != User.TypesOfUser.CompanyRepresentative){
                if(u.getID().equalsIgnoreCase(ID) && u.getPassword().equals(password) && u.isLoggedIn() == false){
                    System.out.println(u.getID());
                    System.out.println(u.getPassword());
                    u.setLogin(true);
                    this.currentUser = u;
                    return true;
                }
            }
            else {
            System.out.println(u.getID());
            System.out.println(u.getPassword());
            if(u.getID().equals(ID) && u.getPassword().equals(password) && u.isLoggedIn() == false){
                u.setLogin(true);
                this.currentUser = u;
                return true;
            }
        }   
    }
        return false;
    }
    
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
    		if(cr.isApproved() == true) {
    		User u = cr;
    		userList.add(u);
    		}
    		else {
    			continue;
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
    
    public boolean approveCompanyRepresentative(String repID){
    	for(CompanyRepresentative cr: repList) {
    		if(cr.getID().equals(repID)) {
    			if(cr.isApproved()) {
    				System.out.println("Error, Cant Accept, CompanyRepresentative Has Already Been Approved!");
    				return false;
    			}
    			else if(cr.isApproved() == null) {
    				cr.setApproved(true);
    				User u = cr;
    				userList.add(u);
    				return true;
    			}
    			else {
    				System.out.println("Error! Cant Accept, CompanyRepresentative Has Already Been Rejected!");
    				return false;
    			}
    		}
    	}
        return true;
    }

    public boolean rejectCompanyRepresentative(String repID){
    	for(CompanyRepresentative cr: repList) {
    		if(cr.getID().equals(repID)) {
    			if(cr.isApproved()) {
    				System.out.println("Error! Cant Reject, CompanyRepresentative Has Already Been Approved!");
    				return false;
    			}
    			else if(cr.isApproved() == null) {
    				cr.setApproved(false);
    				return true;
    			}
    			else {
    				System.out.println("Error! Cant Reject, CompanyRepresentative Has Already Been Rejected!");
    				return false;
    			}
    		}
    	}
        return true;
    }
    
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
    
    public void changePassword(String userID, String oldPassword){
    	User u = userList.stream().filter(s -> s.getID().equals(userID)).filter(s -> s.getPassword().equals(oldPassword)).findFirst().orElse(null);
    	if(u == null) {
    		System.out.println("No Such User/Mismatched UserId and oldPassword");
    	}
    	else {
    		u.setPassword(oldPassword);
            System.out.println("Login again with new password.");
    	}
    }
    
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
}
