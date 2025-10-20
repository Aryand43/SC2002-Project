import controllers.CompanyFileHandler;
import controllers.StaffFileHandler;
import controllers.StudentFileHandler;
import java.util.ArrayList;
import managers.UserManager;
import models.User;
public class Main {
	private static ArrayList<User> userList = new ArrayList<>();
	
	static {
        // Automatically load Career Center Staff
        StaffFileHandler staffFileHandler = new StaffFileHandler();
        ArrayList<CareerCenterStaff> staffList = staffFileHandler.readFromFile();

        userList.addAll(staffList);
        System.out.println("[Init] Registered " + staffList.size() + " Career Center Staff.");
    }
	
    public static ArrayList<User> getAllUsers() {
        return userList;
    }
	
    public static void main (String args[]){
    	
        // Initialise all users as objects
        StudentFileHandler student_fh = new StudentFileHandler();
        CompanyFileHandler company_fh = new CompanyFileHandler();
        StaffFileHandler staff_fh = new StaffFileHandler();

        ArrayList<User> studentList = student_fh.readFromFile();
        ArrayList<User> repList = company_fh.readFromFile();
        ArrayList<User> staffList = staff_fh.readFromFile();
        UserManager UM = new UserManager(studentList, repList, staffList);
        
        User user1 = studentList.get(1); 
        System.out.println(user1.getID());
        user1.setPassword("password");
        UM.login(user1.getID(), user1.getPassword());
        System.out.println("CURRENT USER IS: " + UM.getCurrentUser().getUserName());
        UM.logout();

    }
}
