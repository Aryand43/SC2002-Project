import models.User;
import controllers.studentFileHandler;
public class Main {
    public static void main (String args[]){
        
        // user class testing (not part of project)
        User user1 = new User("U1234567", "Johnny");
        user1.setPassword("randompasstest");
        user1.setPassword("password");
        
        studentFileHandler studentfileHandler = new studentFileHandler();
        studentfileHandler.readFromFile();
    }
}
