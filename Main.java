import controllers.FileHandler;
import java.util.ArrayList;
import managers.InternshipManager;
import managers.UserManager;
import models.Internship;
import models.User;

public class Main {
	private ArrayList<User> studentList = new ArrayList<>();
	private ArrayList<User> staffList = new ArrayList<>();
	private ArrayList<User> repList = new ArrayList<>();
    private static ArrayList<Internship> intershipList = new ArrayList<>();

    public static void main (String args[]){
    	FileHandler fh = new FileHandler()
        // Initialise all users as objects
        studentList = FileHandler.readFromFile();
        repList = .readFromFile();
        staffList = staff_fh.readFromFile();
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
