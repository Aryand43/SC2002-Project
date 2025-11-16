package boundaries;

import java.util.List;
import models.Internship;

public class MenuBoundary {

    /**
     * Print section header
     */
    private void printSectionHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" " + title);
        System.out.println("=".repeat(60));
    }

     /**
     * Print separator line
     */
    private void printSeparator() {
        System.out.println("-".repeat(60));
    }
    
    public void displayLoginMenu() {
    	printSectionHeader("Welcome to Internship Management Portal!");
		System.out.println("========== Portal Menu =========");
		System.out.println("1. Login");
		System.out.println("2. Reset Password (Forgot Password)");
		System.out.println("3. Change Password");
		System.out.println("4. Register (Company Representative)");
		System.out.println("5. Exit");
		printSeparator();
    }
    
    /**
     * Display Student Internship Menu
     */
    public void displayStudentInternshipMenu() {
        printSectionHeader("Student Internship Menu");
        System.out.println("1. View Available Internships");
        System.out.println("2. Search Internships");
        System.out.println("3. Apply for Internship");
        System.out.println("4. View My Applications");
        System.out.println("5. Filter Internships");
        System.out.println("0. Logout");
        printSeparator();
    }
    /**
     * Display list of internships
     * @param internships List of internships to display
     */
    public void displayInternshipList(List<Internship> internships) {
        if (internships.isEmpty()) {
            System.out.println("No internships found.");
            return;
        }
        
        for (int i = 0; i < internships.size(); i++) {
            printSectionHeader("Internship " + (i + 1));
            System.out.printf("%d. %s\n", (i + 1), internships.get(i).getDetailedInfo());
        }
        
        System.out.println("\nTotal: " + internships.size() + " internship(s)");
        printSeparator();
    }
    
    public void displayInternshipFilterMenu(){
        printSectionHeader("Filter By");
        System.out.println(" 1. Date Range");
        System.out.println(" 2. By Internship Level");
        System.out.println(" 3. Custom (status, level, major, company)");
        printSeparator();
    }
    /**
     * Display Career Center Staff Internship Menu
     */
    public void displayStaffInternshipMenu() {
        printSectionHeader("Career Center Staff - Menu");
        System.out.println("1. View Company Representative account list");
        System.out.println("2. Authorize / Reject Company Representative account");
        System.out.println("3. View Internship Opportunities Pending For Approval");
        System.out.println("4. Approve / Reject Internship Opportunity Postings");
        System.out.println("5. View Student Withdrawal Requests");
        System.out.println("6. Approve / Reject Student Withdrawal Requests");
        System.out.println("7. Generate Reports regarding internship opportunities");
        System.out.println("0. Logout");
        printSeparator();
    }

    /**
     * Display Company Representative Internship Menu
     */
    public void displayCompanyRepInternshipMenu() {
        printSectionHeader("Company Representative - Internship Management");
        System.out.println("1. View My Internships");
        System.out.println("2. Create New Internship");
        System.out.println("3. Toggle Internship Visibility");
        System.out.println("4. View Applications");
        System.out.println("5. Approve/Reject Applications");
        System.out.println("0. Logout");
        printSeparator();
    }
}
