package boundaries;
import java.util.Scanner;

import managers.UserManager;

public class UserLoginBoundary {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Welcome to Internship Management Portal!");
		System.out.println("========== Internship Menu =========");
		System.out.println("1. Login");
		System.out.println("2. Reset Password");
		System.out.println("3. Register (Company Representative)");
		System.out.println("0. Exit");
		System.out.println("Please enter your input (1-0)! ");
		
		UserManager userManager = new UserManager();
		int userInput;
		try {
			userInput = sc.nextInt();
	        } catch (Exception e) {
	    System.out.println("Invalid input. Please enter a number.");
	        sc.nextLine();          
	        userInput = -1;          
	        }
		
		 while (userInput != 0) {
		        switch (userInput) {
		            case 1 -> {
		            	sc.nextLine();
		            	System.out.println("Please enter yout ID");
		            	String userInputID = sc.nextLine();
		            	System.out.println("Please enter your Password!");
		            	String userInputPassword = sc.nextLine();
		            	boolean successfulogin = userManager.login(userInputID, userInputPassword);
		            	
		            	if(successfulogin) {
		            		//Display the corresponding user
		            		//Maybe get login() to return tuple of boolean and userid
		            	}
		    			break;
		            }
		            case 2 ->{
		            	//To implement Set & Reset Password FUnction
		            	break;
		            }
		            
		            case 3->{
		            	// To Implement Register Function
		            	break;
		            }
		            
		        }

		        System.out.println("Welcome to Internship Management Portal!");
				System.out.println("========== Internship Menu =========");
				System.out.println("1. Login");
				System.out.println("2. Reset Password");
				System.out.println("3. Register (Company Representative)");
				System.out.println("0. Exit");
				System.out.println("Please enter your input (1-0)! ");
		        try {
		            userInput = sc.nextInt();
		        } catch (Exception e) {
		            System.out.println("Invalid input. Please enter a number.");
		            sc.nextLine();              
		            userInput = -1;             
		        }
		    }
		 	userManager.logout();

		    System.out.println("Exiting Internship Management System. Goodbye!");
		    sc.close();
		}

}
