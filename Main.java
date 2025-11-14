import boundaries.*;
import controllers.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import models.User.TypesOfUser;

public class Main {
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    int inputInteger(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    LocalDate inputDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateInput = scanner.nextLine().trim();
                return LocalDate.parse(dateInput, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy (e.g., 25/12/2024).");
            }
        }
    }
    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        // 1. INITIALIZE ALL MANAGERS (Loads data from CSV files)
        UserManager userManager = new UserManager();
        InternshipManager internshipManager = new InternshipManager();

        // ApplicationManager needs InternshipManager to link applications to opportunities
        //ApplicationManager applicationManager = new ApplicationManager(internshipManager, userManager);
        MenuBoundary UI = new MenuBoundary();
        
        // CANT TEST SHIT FOR NOw
        // CAN START OUT MAIN CODE HERE FOR THE INTERNSHIP MANAGEMENT SYSTEM
        
        //Step 1: Prompt login UI for users to login
        while(true){
            UI.displayLoginMenu();
            
            // Input username
            System.out.print("Enter Username: ");
            String username = sc.nextLine();
            
            // Input password
            System.out.print("Enter password");
            String password = sc.nextLine();

            //Login user function 
            boolean login = userManager.login(username, password);
            if (login) break; //if login succesfull, can get into user specific functions alrdy
        }

        //Step 2: Display respective user's main menu in the internship management system
        TypesOfUser currentPermission = userManager.getCurrentUser().getUserType();
        Main main = new Main(); // Need to create new main obj to call input validation methods
        int choice;

        switch(currentPermission){
            case Student: // Current user is student
                UI.displayStudentInternshipMenu();
                choice = main.inputInteger("Enter choice: ", 0, 5);
                switch (choice){
                    case 1: // View Available Internships
                        
                        break;
                    case 2: // Search Internships
                        
                        break;
                    case 3: // Apply for Internship
                        
                        break;
                    case 4: // View My Applications
                        
                        break;
                    case 5: // Filter Internships
                        
                        break;
                    case 0: // Logout
                        userManager.logout();
                        break; 
                }
                break;
                
            case CareerCenterStaff: // Current user is Career Center Staff
                UI.displayStaffInternshipMenu();
                main.inputInteger("Enter choice: ", 0, 6);
                break;


            case CompanyRepresentative: // Current user is Company Representative
                UI.displayCompanyRepInternshipMenu();
                main.inputInteger("Enter choice: ", 0, 5);
                break;


            default:
                System.out.println("Invalid user type.");
                break;
        }
        System.out.println("Exiting Internship Management System... Goodbye!");
        sc.close();
    }
}