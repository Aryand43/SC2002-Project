package managers;

import java.util.ArrayList;
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
    private ArrayList<User> studentList;
    /**
     * repList stores an array of initialised Company Representative objects by FileHandler
     */
    private ArrayList<User> repList;
    /**
     * staffList stores an array of initialised Career Center Staff objects by FileHandler
     */
    private ArrayList<User> staffList; 
    /**
     * Stores the whole User object
     */
    private User currentUser;

    /**
     * Class Constructor<br>
     * Ensure each object list is loaded accordingly eg. Student list from main code is loaded into Student list in User Manager.
     */
    public UserManager(ArrayList<User> studentList, ArrayList<User> repList, ArrayList<User> staffList){
        this.studentList = studentList;
        this.repList = repList;
        this.staffList = staffList;
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
    public ArrayList<User> getRespectiveUserList(String ID){   
        if (Character.toString(ID.charAt(0)).equalsIgnoreCase("U")){
            return this.studentList;
        }
        else if (Character.toString(ID.charAt(0)).equalsIgnoreCase("CR")){
            return this.repList;
        }
        else{
            return this.staffList;
        }
    }

    /**
     * Login function checks the inputted ID, password and login state.<br>
     * If both ID and password were entered correctly, and login state is false, the user will sucessfully login.<br>
     * Else, user will be unable to login.
     * 
     */
    public void login(String ID, String password){
        ArrayList<User> userList = getRespectiveUserList(ID);

        for(User u: userList){
            // If user is company rep, have to check email for login detials
            if (u.getUserType() != User.TypesOfUser.CompanyRepresentative){
                System.out.println(u.getEmail());
                System.out.println(u.getPassword());
                if(u.getEmail().equals(ID) && u.getPassword().equals(password) && u.isLoggedIn() == false){
                    u.setLogin(true);
                    this.currentUser = u;
                    System.out.printf("\n%s has successfully logged in!\n", u.getUserName());
                    return;
                }
                else if(u.getID().equals(ID) && u.getPassword().equals(password) && u.isLoggedIn() == true){
                    System.out.printf("\nError: %s already logged in!", u.getUserName());
                    return;
                }
            }
            else {
                System.out.println(u.getID());
                System.out.println(u.getPassword());
                if(u.getID().equals(ID) && u.getPassword().equals(password) && u.isLoggedIn() == false){
                    u.setLogin(true);
                    this.currentUser = u;
                    System.out.printf("\n%s has successfully logged in!\n", u.getUserName());
                    return;
                }
                else if(u.getID().equals(ID) && u.getPassword().equals(password) && u.isLoggedIn() == true){
                    System.out.printf("\nError: %s already logged in!", u.getUserName());
                    return;
                }
            }   
        }
        System.out.println("\nError: Wrong Password or ID inputted!");
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
    
    //placeholder for now
    public boolean approveCompanyRepresentative(String repID){
        return true;
    }

    //placeholder for now
    public boolean rejectCompanyRepresentative(String repID){
        return true;
    }
}
