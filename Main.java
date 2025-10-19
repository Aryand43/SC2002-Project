import models.User;
import controllers.studentFileHandler;
import java.util.ArrayList;
public class Main {
	private static ArrayList<User> userList = new ArrayList<>();
    public static void main (String args[]){
    	
        
        // user class testing (not part of project)
        User user1 = new User("U1234567", "Johnny","testemai@gmail.com");
        user1.setPassword("randompasstest");
        user1.setPassword("password");

        
        studentFileHandler studentfileHandler = new studentFileHandler();
        studentfileHandler.readFromFile();
    }
}
