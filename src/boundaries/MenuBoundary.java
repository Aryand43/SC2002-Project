package boundaries;

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
    	System.out.println("Welcome to Internship Management Portal!");
		System.out.println("========== Internship Menu =========");
		System.out.println("1. Login");
		System.out.println("2. Reset Password");
		System.out.println("3. Register (Company Representative)");
		System.out.println("0. Exit");
		System.out.println("Please enter your input (1-0)! ");
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
        System.out.println("0. Back to Main Menu");
        printSeparator();
    }

    /**
     * Display Career Center Staff Internship Menu
     */
    public void displayStaffInternshipMenu() {
        printSectionHeader("Career Center Staff - Internship Management");
        System.out.println("1. View All Internships");
        System.out.println("2. View Pending Internships");
        System.out.println("3. Approve Internship");
        System.out.println("4. Reject Internship");
        System.out.println("5. Filter Internships");
        System.out.println("6. Generate Report");
        System.out.println("0. Back to Main Menu");
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
        System.out.println("0. Back to Main Menu");
        printSeparator();
    }
}
