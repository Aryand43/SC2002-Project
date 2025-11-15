package boundaries;

import java.util.Scanner;

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
