import java.util.Scanner;

public class User {
    private int ID;
    private String name;
    private String password;

    /** 
    * Class constructor.
    */
    public User(int ID, String name, String pw){
        this.ID = ID;
        this.name = name;
        this.password = pw;
    }

    public int getID(){return this.ID;}
    public String getUserName(){return this.name;}
    public String getPassword(){return this.password;}

    public String setUserName(){return this.name;}
    public boolean setPassword(String oldPass){
        Scanner sc = new Scanner(System.in);
        while(true){
            if (oldPass.compareTo(this.password) == 0){
                System.out.print("\nEnter new password: ");
                String newPass = sc.nextLine();
                if (isValidPassword(newPass)) {
                    this.password = newPass;
                    break;
                } 
                else {
                    System.out.println("- At least 8 characters");
                    System.out.println("- At least one uppercase letter");
                    System.out.println("- At least one digit");
                    System.out.println("- At least one special character");
                }
            }
        }   
        sc.close();
        return true;
    }
    
    public static boolean isValidPassword(String password) {
        boolean length = password.length() >= 8; // Password length is at least 8
        boolean upper = password.chars().anyMatch(Character::isUpperCase); // At least 1 upper
        boolean digit = password.chars().anyMatch(Character::isDigit); // At least 1 number
        // Regex to check if there is at least 1 special character
        boolean special = password.matches(".*[!@#$%^&*()_+\\-={};':\"\\\\|,.<>/?].*");

        // ALL conditions met before able to change password
        if (length && upper && digit && special) {
            System.out.println("Valid new password");
            return true;
        } 
        
        else {
            System.out.println("Invalid new password");
            return false;
        }
    }
                

}


