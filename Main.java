import boundaries.*;
import controllers.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import models.*;
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

    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        // 1. INITIALIZE ALL MANAGERS (Loads data from CSV files) & Variables
        UserManager userManager = new UserManager();
        InternshipManager internshipManager = new InternshipManager();

        // ApplicationManager needs InternshipManager and UserManager to link applications to opportunities
        ApplicationManager applicationManager = new ApplicationManager(internshipManager, userManager);

        // Report generator used by staff for reports
        ReportGenerator reportGenerator = new ReportGenerator(applicationManager, internshipManager, userManager);

        MenuBoundary UI = new MenuBoundary();
        Main main = new Main(); // Need to create new main obj to call input validation methods
        // Use the same Scanner instance across main and local code to avoid input stream clashes
        main.scanner = sc;
        int input = 0;

        //MAIN CODE HERE FOR THE INTERNSHIP MANAGEMENT SYSTEM
        //Step 1: Prompt login UI for users to login
        while(input != 5) {
        	boolean login = false;
        	while(!login) {
        		UI.displayLoginMenu();
            	input = main.inputInteger("Please Enter Your Input: ", 1, 5);
            	switch(input) {
            	case 1 -> {
                    // Input username
                    System.out.print("Enter UserID: ");
                    String userID = sc.nextLine();
                    
                    // Input password
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    login = userManager.login(userID, password, sc);
                        }
            	case 2 -> {
                    System.out.print("Enter User ID: ");
                    String userID2 = sc.nextLine();
                    
                    // Input Email
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    
                    userManager.resetPassword(userID2, email);
                        }
            	case 3 -> {
                    System.out.print("Enter UserID: ");
                    String userID3 = sc.nextLine();
                    
                    // Input oldPassword
                    System.out.print("Enter oldpassword: ");
                    String oldPassword = sc.nextLine();
                    
                    userManager.changePassword(userID3, oldPassword, sc);
                        }
            	case 4 -> {
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
                        }
            		
            	case 5 -> {
                        }
            	}
            	if(input == 5) {
            		break;
            	}
        	}
            if(input == 5) {
            	break;
            }
        	 //Step 2: Display respective user's main menu in the internship management system
            TypesOfUser currentPermission = userManager.getCurrentUser().getUserType();
            int choice;
            
        	while(userManager.getCurrentUser() != null) {
                User curUser = userManager.getCurrentUser();
                switch(currentPermission){

                case Student: // Current user is student
                while(true){
                    UI.displayStudentInternshipMenu();
                    int studentYr = ((Student)curUser).getYearOfStudy();
                    String studentMajor = ((Student)curUser).getMajor();
                    System.out.printf("\n%s current year of study: %d", curUser.getUserName(), studentYr);
                    System.out.printf("\n%s current major: %s\n\n", curUser.getUserName(), studentMajor);
                    choice = main.inputInteger("Enter choice: ", 0, 7);
                    List<Application> myApps = ((Student)curUser).getApplications();
                    
                    List <Internship> ListingsVisibleToStudent = internshipManager.getVisibleInternshipsForStudent(studentYr, studentMajor);

                    switch (choice){
                        case 1 -> // View Available Internships
                            UI.displayInternshipList(ListingsVisibleToStudent);

                        case 2 -> {
                            // Search Internships (By keyword)
                            UI.displayInternshipList(ListingsVisibleToStudent);
                            System.out.print("Enter keyword to search: ");
                            String keywordSearch = sc.nextLine().trim();
                            List<Internship> keywordList = internshipManager.search(keywordSearch);
                            System.out.print("Displaying search results for keyword: " + keywordSearch);
                            UI.displayInternshipList(keywordList);
                        }

                        case 3 -> {
                            // Apply for Internship
                            System.out.print("Enter Internship ID to apply: ");
                            
                            String internshipID = sc.nextLine().trim();
                            Internship internshipToApply = internshipManager.findInternshipByID(internshipID);
                            if (internshipToApply == null) {
                                System.out.println("Error: Internship with ID '" + internshipID + "' not found.");
                                break;
                            }
                            // Validate that student can apply (year/level restrictions, application limit, etc.)
                            boolean canApply = ((Student)curUser).canApply(internshipToApply);
                            if(canApply){
                                applicationManager.apply((Student)curUser, internshipToApply);
                                System.out.println("Application submitted successfully for Internship ID: " + internshipID);
                            } else {
                                System.out.println("Cannot apply for this internship. See message above for details.");
                            }
                        }
                        case 4 -> {
                            List<Application> myApplications = applicationManager.getApplicationList().stream()
                                .filter(a -> a.getStudentID().equals(curUser.getID()))
                                .toList();
                            
                            UI.displayApplicationList(myApplications);

                            //Add it to student object, with the respective applications
                            for(Application a : myApplications){
                                ((Student)curUser).addApplication(a);
                            }
                            
                            // After displaying applications, check if more than one is successful
                            List<Application> successfulApps = myApplications.stream()
                                .filter(a -> a.getStatus() == Application.ApplicationStatus.SUCCESSFUL)
                                .toList();
                            if (successfulApps.size() > 1) {
                                System.out.println("You have multiple accepted internships. Please pick one to keep:");
                                for (int i = 0; i < successfulApps.size(); i++) {
                                    System.out.printf("%d. %s (InternshipID: %s)\n", i + 1, successfulApps.get(i).getID(), successfulApps.get(i).getInternshipID());
                                }
                                int pick = main.inputInteger("Enter the number of the internship to accept: ", 1, successfulApps.size());
                                Application chosen = successfulApps.get(pick - 1);
                                // Set the rest to UNSUCCESSFUL
                                for (Application app : successfulApps) {
                                    if (app != chosen) {
                                        app.setStatus(Application.ApplicationStatus.WITHDRAWN);
                                    }
                                }
                                System.out.println("Your choice has been saved. Only one internship is now accepted.");
                            }
                        }
                        case 5 -> {    // Filter Internships
                            System.out.println("\nFilter Internships by:");
                            System.out.println("1. Level (BASIC/INTERMEDIATE/ADVANCED)");
                            System.out.println("2. Company Name");
                            System.out.println("3. Both Level and Company");
                            System.out.println("0. Back");
                            int filterChoice = main.inputInteger("Select filter option: ", 0, 3);

                            List<Internship> filteredList = null;

                            if (filterChoice == 1) {
                                System.out.print("Enter level (BASIC/INTERMEDIATE/ADVANCED): ");
                                String levelStr = sc.nextLine().trim().toUpperCase();
                                try {
                                    Internship.InternshipLevel level = Internship.InternshipLevel.valueOf(levelStr);
                                    List<Internship> visibleInternships = internshipManager.getVisibleInternshipsForStudent(studentYr, studentMajor);
                                    filteredList = visibleInternships.stream()
                                        .filter(i -> i.getLevel() == level)
                                        .toList();
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid level. Please use BASIC, INTERMEDIATE, or ADVANCED.");
                                    filteredList = java.util.Collections.emptyList();
                                }
                            } else if (filterChoice == 2) {
                                System.out.print("Enter company name: ");
                                String company = sc.nextLine().trim();
                                List<Internship> visibleInternships = internshipManager.getVisibleInternshipsForStudent(studentYr, studentMajor);
                                filteredList = visibleInternships.stream()
                                    .filter(i -> i.getCompanyName().toLowerCase().contains(company.toLowerCase()))
                                    .toList();
                            } else if (filterChoice == 3) {
                                System.out.print("Enter level (BASIC/INTERMEDIATE/ADVANCED): ");
                                String levelStr = sc.nextLine().trim().toUpperCase();
                                System.out.print("Enter company name: ");
                                String company = sc.nextLine().trim();
                                try {
                                    Internship.InternshipLevel level = Internship.InternshipLevel.valueOf(levelStr);
                                    List<Internship> visibleInternships = internshipManager.getVisibleInternshipsForStudent(studentYr, studentMajor);
                                    filteredList = visibleInternships.stream()
                                        .filter(i -> i.getLevel() == level)
                                        .filter(i -> i.getCompanyName().toLowerCase().contains(company.toLowerCase()))
                                        .toList();
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid level. Please use BASIC, INTERMEDIATE, or ADVANCED.");
                                    filteredList = java.util.Collections.emptyList();
                                }
                            } else if (filterChoice == 0) {
                                // Back to previous menu
                                break;
                            }
                            // Always print the table, even if empty
                            System.out.println("\nFiltered Results:");
                            UI.displayInternshipList(filteredList == null ? Collections.emptyList() : filteredList);
                        }
                        case 6 -> {       
                            // Automatically approve all pending applications and internships for this student
                            List<Application> studentApplicationsCase6 = ((Student)curUser).getApplications();
                            boolean anyApproved = false;
                            for (Application app : studentApplicationsCase6) {
                                if (app.getStatus() == Application.ApplicationStatus.PENDING) {
                                    app.setStatus(Application.ApplicationStatus.SUCCESSFUL);

                                if (app.getInternship() != null) {
                                        app.getInternship().setStatus(models.Internship.InternshipStatus.APPROVED);
                                    }
                                    anyApproved = true;
                                }
                            }
                            if (anyApproved) {
                                System.out.println("All your pending applications have been approved and marked successful.");
                            } else {
                                System.out.println("No pending applications to approve.");
                            }

                            // Let student pick one internship if more than one is SUCCESSFUL
                            List<Application> studentApplications = ((Student)curUser).getApplications();
                            List<Application> successfulAppsCase6 = studentApplications.stream()
                                .filter(a -> a.getStatus() == Application.ApplicationStatus.SUCCESSFUL)
                                .toList();
                            if (successfulAppsCase6.size() > 1) {
                                System.out.println("You have multiple accepted internships. Please pick one to keep:");
                                for (int i = 0; i < successfulAppsCase6.size(); i++) {
                                    System.out.printf("%d. %s (InternshipID: %s)\n", i + 1, successfulAppsCase6.get(i).getID(), successfulAppsCase6.get(i).getInternshipID());
                                }
                                int pick = main.inputInteger("Enter the number of the internship to accept: ", 1, successfulAppsCase6.size());
                                Application chosen = successfulAppsCase6.get(pick - 1);
                                // Set the rest to WITHDRAWN and increment available slots
                                for (Application app : successfulAppsCase6) {
                                    if (app != chosen) {
                                        app.setStatus(Application.ApplicationStatus.WITHDRAWN);
                                        if (app.getInternship() != null) {
                                            app.getInternship().decrementConfirmedSlots();
                                        }
                                    }
                                }
                                System.out.println("Your choice has been saved. Other accepted internships have been withdrawn and are now available for other students.");
                            }
                        }
                        case 7 -> {
                            // Withdraw Application
                            myApps = ((Student)curUser).getApplications();
                            List<Application> pendingOrSuccessfulApps = myApps.stream()
                                .filter(a -> a.getStatus() == Application.ApplicationStatus.PENDING ||
                                             a.getStatus() == Application.ApplicationStatus.SUCCESSFUL)
                                .toList();
                            if (pendingOrSuccessfulApps.isEmpty()) {
                                System.out.println("You have no pending or successful applications to withdraw.");
                                break;
                            }
                            UI.displayApplicationList(pendingOrSuccessfulApps);
                            System.out.print("Enter Application ID to withdraw: ");
                            String appIdToWithdraw = sc.nextLine().trim();
                            Application appToWithdraw = applicationManager.findByID(appIdToWithdraw);
                            if (appToWithdraw != null && (appToWithdraw.getStatus() == Application.ApplicationStatus.PENDING ||
                                                         appToWithdraw.getStatus() == Application.ApplicationStatus.SUCCESSFUL)) {
                                applicationManager.requestWithdrawal(appToWithdraw);
                                System.out.println("Withdrawal request submitted for Application ID: " + appIdToWithdraw);
                            } else {
                                System.out.println("Invalid Application ID or cannot withdraw this application.");
                            }
                        }
                        case 0 ->{// Logout
                            userManager.logout();
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                }
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
                             case 1 -> // View Company Representative account list
                                 printCompanyRepList(userManager.getRepList().stream().filter(s ->s.isApproved()==null).toList());
                             case 2 -> {
                                 // Authorize / Reject Company Representative
                                 System.out.print("Enter Company Representative ID to review: ");
                                 String repId = sc.nextLine().trim();
                                 System.out.println("1. Approve\n2. Reject");
                                 int decision = main.inputInteger("Choose action: ", 1, 2);
                                 if (decision == 1) {
                                     boolean ok = userManager.approveCompanyRepresentative(repId);
                                     if (ok) {
                                         System.out.println("Company Representative approved successfully.");
                                     }
                                     // Error message already printed by UserManager
                                 } else {
                                     boolean ok = userManager.rejectCompanyRepresentative(repId);
                                     if (ok) {
                                         System.out.println("Company Representative rejected successfully.");
                                     }
                                     // Error message already printed by UserManager
                                 }
                        }
                             case 3 -> // View Pending Internships
                                 UI.displayInternshipList(internshipManager.getPendingInternships());
                             case 4 -> {
                                 // Approve / Reject Internship Opportunity Postings
                                 System.out.print("Enter Internship ID: ");
                                 String internshipId = sc.nextLine().trim();
                                 System.out.println("1. Approve\n2. Reject");
                                 int ir = main.inputInteger("Choose action: ", 1, 2);
                                 if (ir == 1) {
                                     boolean ok = internshipManager.approveListing(internshipId);
                                     if (ok) {
                                         System.out.println("Internship listing approved successfully.");
                                     }
                                     // Error message already printed by InternshipManager
                                 } else {
                                     boolean ok = internshipManager.rejectListing(internshipId);
                                     if (ok) {
                                         System.out.println("Internship listing rejected successfully.");
                                     }
                                     // Error message already printed by InternshipManager
                                 }
                        }
                             case 5 -> // View Student Withdrawal Requests
                                 UI.displayApplicationList(applicationManager.getApplicationList().stream()
                                     .filter(a -> a.getStatus() == models.Application.ApplicationStatus.WITHDRAW_REQUESTED)
                                     .toList());
                             case 6 -> {
                                 // Approve / Reject Student Withdrawal Requests
                                 java.util.List<models.Application> withdraws = applicationManager.getApplicationList().stream()
                                         .filter(a -> a.getStatus() == models.Application.ApplicationStatus.WITHDRAW_REQUESTED)
                                         .toList();
                                 if (withdraws.isEmpty()) {
                                     System.out.println("No withdrawal requests found.");
                                     break;
                                 }
                                 UI.displayApplicationList(withdraws);
                                 System.out.print("Enter Application ID to act on: ");
                                 String appId = sc.nextLine().trim();
                                 System.out.println("1. Approve Withdrawal\n2. Reject Withdrawal");
                                 int wa = main.inputInteger("Choose action: ", 1, 2);
                                 if (wa == 1) {
                                     boolean ok = applicationManager.approveStudentWithdrawal(appId);
                                     if (ok) {
                                         System.out.println("Withdrawal approved successfully.");
                                     }
                                     // Error message already printed by ApplicationManager
                                 } else {
                                     boolean ok = applicationManager.rejectStudentWithdrawal(appId);
                                     if (ok) {
                                         System.out.println("Withdrawal rejected successfully.");
                                     }
                                     // Error message already printed by ApplicationManager
                                 }
                        }
                             case 7 -> {
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
                                     case 0 -> {
                                    }
                                     case 1 ->  {
                                         System.out.print("Enter preferred major: ");
                                         String major = sc.nextLine().trim();
                                         UI.displayInternshipList(reportGenerator.generateReportByPreferredMajor(major));
                                     }
                                     case 2 ->  {
                                         System.out.print("Enter internship level (BASIC/INTERMEDIATE/ADVANCED): ");
                                         String level = sc.nextLine().trim();
                                         UI.displayInternshipList(reportGenerator.generateReportByLevel(level));
                                     }
                                     case 3 ->  {
                                         System.out.print("Enter status (PENDING/APPROVED/REJECTED/FILLED): ");
                                         String status = sc.nextLine().trim();
                                         UI.displayInternshipList(reportGenerator.generateReportByStatus(status));
                                     }
                                     case 4 ->  {
                                         System.out.print("Enter company name: ");
                                         String company = sc.nextLine().trim();
                                         UI.displayInternshipList(reportGenerator.generateReportByCompany(company));
                                     }
                                     case 5 ->  {
                                         System.out.print("Enter student ID: ");
                                         String studentId = sc.nextLine().trim();
                                         List<Application> apps = reportGenerator.generateReportByStudent(studentId);
                                         UI.displayApplicationList(apps);
                                     }
                                     case 6 ->  {
                                         System.out.print("Enter status (or leave blank): ");
                                         String sStatus = sc.nextLine().trim();
                                         System.out.print("Enter level (or leave blank): ");
                                         String sLevel = sc.nextLine().trim();
                                         System.out.print("Enter preferred major (or leave blank): ");
                                         String sMajor = sc.nextLine().trim();
                                         System.out.print("Enter company name (or leave blank): ");
                                         String sCompany = sc.nextLine().trim();
                                         UI.displayInternshipList(reportGenerator.generateCustomReport(
                                                 sStatus.isEmpty() ? null : sStatus,
                                                 sLevel.isEmpty() ? null : sLevel,
                                                 sMajor.isEmpty() ? null : sMajor,
                                                 sCompany.isEmpty() ? null : sCompany));
                                     }
                                     case 7 ->  {
                                         System.out.println(reportGenerator.generateSummaryReport());
                                     }
                                 }
                        }
                             default -> System.out.println("Unknown option.");
                         }
                     }
                     break;
                 case CompanyRepresentative: // Current user is Company Representative
                    CompanyRepBoundary crBoundary = new CompanyRepBoundary(
                        sc, 
                        (CompanyRepresentative)curUser,
                        internshipManager,
                        applicationManager
                    );
                    crBoundary.run();
                    break;


                default:
                    System.out.println("Invalid user type.");
                    break;
                    }		 
        		 }
        	}
        System.out.println("Exiting Internship Management System... Goodbye!");
        userManager.saveToFile();
        internshipManager.saveToFile();
        applicationManager.saveTofile();
        sc.close();
        }

    /* Helper: print company rep list */
    private static void printCompanyRepList(java.util.List<models.CompanyRepresentative> reps) {
        if (reps == null || reps.isEmpty()) {
            System.out.println("No company representatives found.");
            return;
        }
        System.out.printf("%-12s %-25s %-20s%n", "ID", "Name", "Company");
        
        for (models.CompanyRepresentative r : reps) {
            System.out.printf("%-12s %-25s %-20s%n",
                r.getID(), r.getUserName(), r.getCompanyName());
        }
    }
}