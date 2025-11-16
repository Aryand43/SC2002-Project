import boundaries.*;
import controllers.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import models.*;
import models.User.TypesOfUser;


public class Main {
    private Scanner scanner = new Scanner(System.in);
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

        // ApplicationManager needs InternshipManager and UserManager to link applications to opportunities
        ApplicationManager applicationManager = new ApplicationManager(internshipManager, userManager);

        // Report generator used by staff for reports
        ReportGenerator reportGenerator = new ReportGenerator(applicationManager, internshipManager, userManager);

        MenuBoundary UI = new MenuBoundary();
        Main main = new Main(); // Need to create new main obj to call input validation methods
        int input = 0;
        // CANT TEST SHIT FOR NOw
        // CAN START OUT MAIN CODE HERE FOR THE INTERNSHIP MANAGEMENT SYSTEM
        
        //Step 1: Prompt login UI for users to login
        while(input != 5) {
        	boolean login = false;
        	while(!login) {
        		UI.displayLoginMenu();
            	input = main.inputInteger("Please Enter Your Input: ", 1, 4);
            	switch(input) {
            	case 1:
            		 // Input username
                    System.out.print("Enter UserID: ");
                    String userID = sc.nextLine();
                    
                    // Input password
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    userManager.login(userID, password);

                    if (userManager.getCurrentUser().isPasswordChanged() == true){
                        login = true;
                        System.out.println("Login Successful! Welcome " + userManager.getCurrentUser().getUserName() + "!");
                    }
                    else if (userManager.getCurrentUser().isPasswordChanged() == false){
                        userManager.changePassword(userManager.getCurrentUser().getID(), password);
                    }
                    break;
            	case 2:
            		 System.out.print("Enter User ID: ");
                     String userID2 = sc.nextLine();
                     
                     // Input Email
                     System.out.print("Enter Email: ");
                     String email = sc.nextLine();
                     
            		 userManager.resetPassword(userID2, email);
            		 break;
            	case 3:
            		 System.out.print("Enter UserID: ");
                     String userID3 = sc.nextLine();
                     
                     // Input oldPassword
                     System.out.print("Enter oldpassword: ");
                     String oldPassword = sc.nextLine();
                     
                     userManager.changePassword(userID3, oldPassword);
                     break;
            	case 4:
            		System.out.println("Enter Username: ");
            		String username = sc.nextLine();
            		
            		System.out.println("Enter Company Name: ");
            		String companyName = sc.nextLine();
            		
            		System.out.println("Enter Department Name: ");
            		String DepartmentName = sc.nextLine();
            		
            		System.out.println("Enter Your Position: ");
            		String userPosition = sc.nextLine();
            		
            		System.out.println("Enter Your Email: ");
            		String userEmail = sc.nextLine();
            		
            		CompanyRepresentative newCR = CompanyRepresentative.registerRep(username, companyName, DepartmentName, userPosition, userEmail);
            		userManager.addCompanyRepresentative(newCR);
            		break;
            		
            	case 5:
            		break;
            	}
            	if(input == 5) {
            		break;
            	}
        	}
        	 //Step 2: Display respective user's main menu in the internship management system
            TypesOfUser currentPermission = userManager.getCurrentUser().getUserType();
            int choice;
            
        	while(userManager.getCurrentUser() != null) {
        		 switch(currentPermission){
                 case Student: // Current user is student
                     UI.displayStudentInternshipMenu();
                     choice = main.inputInteger("Enter choice: ", 0, 5);
                     switch (choice){
                         case 1: // View Available Internships
                            int studentYr = ((models.Student)userManager.getCurrentUser()).getYearOfStudy();
                            String studentMajor = ((models.Student)userManager.getCurrentUser()).getMajor();
                            internshipManager.getVisibleInternshipsForStudent(studentYr, studentMajor);
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
                     // Loop the staff menu until logout
                     while (true) {
                         UI.displayStaffInternshipMenu();
                         int staffChoice = main.inputInteger("Enter choice: ", 0, 7);
                         if (staffChoice == 0) {
                             userManager.logout();
                             break;
                         }
                         switch (staffChoice) {
                             case 1:
                                 // View Company Representative account list
                                 printCompanyRepList(userManager.getRepList());
                                 break;
                             case 2:
                                 // Authorize / Reject Company Representative
                                 System.out.print("Enter Company Representative ID to review: ");
                                 String repId = sc.nextLine().trim();
                                 System.out.println("1. Approve\n2. Reject");
                                 int decision = main.inputInteger("Choose action: ", 1, 2);
                                 if (decision == 1) {
                                     boolean ok = userManager.approveCompanyRepresentative(repId);
                                     System.out.println(ok ? "Company Representative approved." : "Failed to approve (check ID).");
                                 } else {
                                     boolean ok = userManager.rejectCompanyRepresentative(repId);
                                     System.out.println(ok ? "Company Representative rejected." : "Failed to reject (check ID).");
                                 }
                                 break;
                             case 3:
                                 // View Pending Internships
                                 printInternshipList(internshipManager.getPendingInternships());
                                 break;
                             case 4:
                                 // Approve / Reject Internship Opportunity Postings
                                 System.out.print("Enter Internship ID: ");
                                 String internshipId = sc.nextLine().trim();
                                 System.out.println("1. Approve\n2. Reject");
                                 int ir = main.inputInteger("Choose action: ", 1, 2);
                                 if (ir == 1) {
                                     boolean ok = internshipManager.approveListing(internshipId);
                                     System.out.println(ok ? "Internship listing approved." : "Failed to approve internship listing.");
                                 } else {
                                     boolean ok = internshipManager.rejectListing(internshipId);
                                     System.out.println(ok ? "Internship listing rejected." : "Failed to reject internship listing.");
                                 }
                                 break;
                             case 5:
                                 // View Student Withdrawal Requests
                                 printApplicationList(applicationManager.getApplicationList().stream()
                                     .filter(a -> a.getStatus() == models.Application.ApplicationStatus.WITHDRAW_REQUESTED)
                                     .toList());
                                 break;
                             case 6:
                                 // Approve / Reject Student Withdrawal Requests
                                 java.util.List<models.Application> withdraws = applicationManager.getApplicationList().stream()
                                     .filter(a -> a.getStatus() == models.Application.ApplicationStatus.WITHDRAW_REQUESTED)
                                     .toList();
                                 if (withdraws.isEmpty()) {
                                     System.out.println("No withdrawal requests found.");
                                     break;
                                 }
                                 printApplicationList(withdraws);
                                 System.out.print("Enter Application ID to act on: ");
                                 String appId = sc.nextLine().trim();
                                 System.out.println("1. Approve Withdrawal\n2. Reject Withdrawal");
                                 int wa = main.inputInteger("Choose action: ", 1, 2);
                                 if (wa == 1) {
                                     boolean ok = applicationManager.approveStudentWithdrawal(appId);
                                     System.out.println(ok ? "Withdrawal approved." : "Failed to approve withdrawal (check ID).");
                                 } else {
                                     boolean ok = applicationManager.rejectStudentWithdrawal(appId);
                                     System.out.println(ok ? "Withdrawal rejected." : "Failed to reject withdrawal (check ID).");
                                 }
                                 break;
                             case 7:
                                 // Generate Reports (expose all ReportGenerator methods)
                                 System.out.println("Report types:");
                                 System.out.println(" 1. By Preferred Major");
                                 System.out.println(" 2. By Internship Level");
                                 System.out.println(" 3. By Internship Status");
                                 System.out.println(" 4. By Company Name");
                                 System.out.println(" 5. By Student ID");
                                 System.out.println(" 6. Custom (status, level, major, company)");
                                 System.out.println(" 7. Summary Report");
                                 System.out.println(" 0. Back");
                                 int rpt = main.inputInteger("Select report type: ", 0, 7);
                                 switch (rpt) {
                                     case 0:
                                         break;
                                     case 1: {
                                         System.out.print("Enter preferred major: ");
                                         String major = sc.nextLine().trim();
                                         printInternshipList(reportGenerator.generateReportByPreferredMajor(major));
                                         break;
                                     }
                                     case 2: {
                                         System.out.print("Enter internship level (BASIC/INTERMEDIATE/ADVANCED): ");
                                         String level = sc.nextLine().trim();
                                         printInternshipList(reportGenerator.generateReportByLevel(level));
                                         break;
                                     }
                                     case 3: {
                                         System.out.print("Enter status (PENDING/APPROVED/REJECTED/FILLED): ");
                                         String status = sc.nextLine().trim();
                                         printInternshipList(reportGenerator.generateReportByStatus(status));
                                         break;
                                     }
                                     case 4: {
                                         System.out.print("Enter company name: ");
                                         String company = sc.nextLine().trim();
                                         printInternshipList(reportGenerator.generateReportByCompany(company));
                                         break;
                                     }
                                     case 5: {
                                         System.out.print("Enter student ID: ");
                                         String studentId = sc.nextLine().trim();
                                         java.util.List<models.Application> apps = reportGenerator.generateReportByStudent(studentId);
                                         printApplicationList(apps);
                                         break;
                                     }
                                     case 6: {
                                         System.out.print("Enter status (or leave blank): ");
                                         String sStatus = sc.nextLine().trim();
                                         System.out.print("Enter level (or leave blank): ");
                                         String sLevel = sc.nextLine().trim();
                                         System.out.print("Enter preferred major (or leave blank): ");
                                         String sMajor = sc.nextLine().trim();
                                         System.out.print("Enter company name (or leave blank): ");
                                         String sCompany = sc.nextLine().trim();
                                         printInternshipList(reportGenerator.generateCustomReport(
                                             sStatus.isEmpty() ? null : sStatus,
                                             sLevel.isEmpty() ? null : sLevel,
                                             sMajor.isEmpty() ? null : sMajor,
                                             sCompany.isEmpty() ? null : sCompany));
                                         break;
                                     }
                                     case 7: {
                                         System.out.println(reportGenerator.generateSummaryReport());
                                         break;
                                     }
                                 }
                                 break;
                             default:
                                 System.out.println("Unknown option.");
                         }
                     }
                     break;
                 case CompanyRepresentative: // Current user is Company Representative
                     UI.displayCompanyRepInternshipMenu();
                     main.inputInteger("Enter choice: ", 0, 5);
                     break;


                 default:
                     System.out.println("Invalid user type.");
                     break;
                     }		 
        		 }
        	}
        System.out.println("Exiting Internship Management System... Goodbye!");
        sc.close();
    }

    /* Helper: print company rep list */
    private static void printCompanyRepList(java.util.List<models.CompanyRepresentative> reps) {
        if (reps == null || reps.isEmpty()) {
            System.out.println("No company representatives found.");
            return;
        }
        System.out.printf("%-12s %-25s %-20s %-10s%n", "ID", "Name", "Company", "Approved");
        for (models.CompanyRepresentative r : reps) {
            System.out.printf("%-12s %-25s %-20s %-10s%n",
                r.getID(), r.getUserName(), r.getCompanyName(), r.isApproved() ? "Yes" : "No");
        }
    }

    /* Helper: print internships */
    private static void printInternshipList(java.util.List<models.Internship> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No internships found.");
            return;
        }
        System.out.printf("%-10s %-30s %-10s %-20s %-12s%n", "ID", "Title", "Level", "Company", "Status");
        for (models.Internship i : list) {
            System.out.printf("%-10s %-30s %-10s %-20s %-12s%n",
                i.getInternshipID(), i.getTitle(), i.getLevel(), i.getCompanyName(), i.getStatus());
        }
    }

    /* Helper: print application list */
    private static void printApplicationList(java.util.List<models.Application> apps) {
        if (apps == null || apps.isEmpty()) {
            System.out.println("No applications to show.");
            return;
        }
        System.out.printf("%-12s %-12s %-12s %-15s%n", "AppID", "StudentID", "InternshipID", "Status");
        for (models.Application a : apps) {
            System.out.printf("%-12s %-12s %-12s %-15s%n", a.getID(), a.getStudentID(), a.getInternshipID(), a.getStatus());
        }
    }
}