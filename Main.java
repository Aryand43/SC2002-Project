import controllers.CompanyFileHandler;
import controllers.InternshipFileHandler;
import controllers.StaffFileHandler;
import controllers.StudentFileHandler;
import java.util.ArrayList;
import managers.InternshipManager;
import managers.UserManager;
import models.Internship;
import models.User;

public class Main {
	private static ArrayList<User> userList = new ArrayList<>();
    public static void main (String args[]){
    	
        // Initialise all users as objects
        StudentFileHandler student_fh = new StudentFileHandler();
        CompanyFileHandler company_fh = new CompanyFileHandler();
        StaffFileHandler staff_fh = new StaffFileHandler();

        ArrayList<User> studentList = student_fh.readFromFile();
        ArrayList<User> repList = company_fh.readFromFile();
        ArrayList<User> staffList = staff_fh.readFromFile();
        UserManager UM = new UserManager(studentList, repList, staffList);
        

        //TESTING USER & USER MANAGER
        // User user1 = studentList.get(1); 
        // System.out.println(user1.getID());
        // user1.setPassword("password");
        // UM.login(user1.getID(), user1.getPassword());
        // System.out.println("CURRENT USER IS: " + UM.getCurrentUser().getUserName());
        // UM.logout();

        //TESTING INTERNSHIP & INTERNSHIP MANAGER
        InternshipFileHandler intern_fh = new InternshipFileHandler();
        InternshipManager IM = new InternshipManager(intern_fh);
        IM.displayInternships();
        Internship result = IM.findInternshipByID("INT0002");
        System.out.print(result.getDetailedInfo());
        IM.getPendingInternships();

    }
}
