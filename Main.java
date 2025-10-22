import controllers.CompanyFileHandler;
import controllers.StaffFileHandler;
import controllers.studentFileHandler;
import java.util.ArrayList;
import managers.UserManager;
import models.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 

public class Main {
	private static ArrayList<User> userList = new ArrayList<>();
    public static void main (String args[]){
    	
        // Initialise all users as objects
        studentFileHandler student_fh = new StudentFileHandler();
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
