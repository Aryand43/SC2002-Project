package managers;

import java.util.ArrayList;

import controllers.FileHandler;
import controllers.InternshipSerializer;
import models.User;
import models.Student;
import models.CompanyRepresentative;
import models.Internship;
import models.CareerCenterStaff;
import controllers.StaffSerializer;
import controllers.StudentSerializer;
import controllers.CompanyRepresentativeSerializer;

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
     * Stores the whole User object
     */
    private User currentUser;

    /**
     * Class Constructor<br>
     * Ensure each object list is loaded accordingly eg. Student list from main code is loaded into Student list in User Manager.
     */
    public UserManager(){
        StaffSerializer staffSerializer = new StaffSerializer();
    	FileHandler<CareerCenterStaff> staffFileHandler = new FileHandler<>(staffSerializer);
    	staffList = staffFileHandler.readFromFile();
    	StudentSerializer studentSerializer = new StudentSerializer();
    	FileHandler<Student> studentFileHandler = new FileHandler<>(studentSerializer);
    	studentList = studentFileHandler.readFromFile();
    	CompanyRepresentativeSerializer companyRepSerializer = new CompanyRepresentativeSerializer();
    	FileHandler<CompanyRepresentative> companyFileHandler = new FileHandler<>(companyRepSerializer);
    	repList = companyFileHandler.readFromFile();
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
    //public ArrayList<User> getRespectiveUserList(String ID){   
        //if (Character.toString(ID.charAt(0)).equalsIgnoreCase("U")){
            //return this.studentList;
        //}
        //else if (Character.toString(ID.charAt(0)).equalsIgnoreCase("CR")){
            //return this.repList;
        //}
        //else{
           // return this.staffList;
        //}
    //}

    /**
     * Login function checks the inputted ID, password and login state.<br>
     * If both ID and password were entered correctly, and login state is false, the user will sucessfully login.<br>
     * Else, user will be unable to login.
     * 
     */
    public void login(String ID, String password){
         ArrayList<User> userList = getRespectiveUserList();

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
    
    public ArrayList<User> getRespectiveUserList(){
    	ArrayList<User> userList = new ArrayList<>();
    	
    	for(Student s: studentList) {
    		User u = s;
    		userList.add(u);
    	}
    	
    	for(CompanyRepresentative cr: repList) {
    		User u = cr;
    		userList.add(u);
    	}
    	
    	for(CareerCenterStaff staff: staffList) {
    		User u = staff;
    		userList.add(u);
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
    
    //placeholder for now
    public boolean approveCompanyRepresentative(String repID){
        return true;
    }

    //placeholder for now
    public boolean rejectCompanyRepresentative(String repID){
        return true;
    }

    /**
     * Return list of students (concrete Student objects) stored in manager
     */
    public ArrayList<Student> getStudentList(){
        ArrayList<Student> students = new ArrayList<>();
        for (User u : this.studentList){
            try {
                students.add((Student) u);
            } catch (ClassCastException e){
                // skip or log if element isn't Student
            }
        }
        return students;
    }

    /**
     * Return list of company representatives (concrete CompanyRepresentative objects)
     */
    public ArrayList<CompanyRepresentative> getRepList(){
        ArrayList<CompanyRepresentative> reps = new ArrayList<>();
        for (User u : this.repList){
            try {
                reps.add((CompanyRepresentative) u);
            } catch (ClassCastException e){
                // skip or log
            }
        }
        return reps;
    }

    /**
     * Return list of career center staff (concrete CareerCenterStaff objects)
     */
    public ArrayList<CareerCenterStaff> getStaffList(){
        ArrayList<CareerCenterStaff> staff = new ArrayList<>();
        for (User u : this.staffList){
            try {
                staff.add((CareerCenterStaff) u);
            } catch (ClassCastException e){
                // skip or log
            }
        }
        return staff;
    }
}
