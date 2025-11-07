import managers.UserManager;
import managers.InternshipManager;
import managers.ApplicationManager;
import models.Student;
import models.Internship;
import models.Application;

import java.util.List;

public class Main {

    public static void main (String[] args){
        
        // 1. INITIALIZE ALL MANAGERS (Loads data from CSV files)
        UserManager userManager = new UserManager();
        InternshipManager internshipManager = new InternshipManager();
        // ApplicationManager needs InternshipManager to link applications to opportunities
        ApplicationManager applicationManager = new ApplicationManager(internshipManager, userManager);

        System.out.println("=".repeat(60));
        System.out.println("--- Functional Integration Test: Student Application Limit ---");
        System.out.println("=".repeat(60));

        Student student = null;
        for (Student s : userManager.getStudentList()) {
            if (s.getID().equals("U2310002B")) { // Ng Jia Hao, Year 3, CSC
                student = s;
                break;
            }
        }
        
        if (student == null) {
            System.err.println("FATAL ERROR: Student U2310002B not found in data files. Aborting test.");
            return;
        }

        Internship int0001 = internshipManager.findInternshipByID("INT0001");
        Internship int0002 = internshipManager.findInternshipByID("INT0002");
        Internship int0003 = internshipManager.findInternshipByID("INT0003");
        Internship int0005 = internshipManager.findInternshipByID("INT0005");
        
        if (int0001 == null || int0002 == null || int0003 == null || int0005 == null) {
            System.err.println("FATAL ERROR: One or more required Internship IDs (INT0001, INT0002, INT0003, INT0005) not found. Aborting test.");
            return;
        }

        System.out.printf("Testing Student: %s (%s, Year %d)\n", student.getUserName(), student.getID(), student.getYearOfStudy());
        System.out.println("-".repeat(60));

        // 3. SCENARIO: Apply for 3 internships
        
        // Apply 1
        Application app1 = applicationManager.apply(student.getID(), int0001.getInternshipID());
        boolean success1 = student.applyForInternship(app1);
        System.out.printf("Attempt 1 (INT0001): %s\n", success1 ? "SUCCESS" : "FAILURE");

        // Apply 2
        Application app2 = applicationManager.apply(student.getID(), int0002.getInternshipID());
        boolean success2 = student.applyForInternship(app2);
        System.out.printf("Attempt 2 (INT0002): %s\n", success2 ? "SUCCESS" : "FAILURE");

        // Apply 3
        Application app3 = applicationManager.apply(student.getID(), int0003.getInternshipID());
        boolean success3 = student.applyForInternship(app3);
        System.out.printf("Attempt 3 (INT0003): %s\n", success3 ? "SUCCESS" : "FAILURE");
        
        System.out.println("-".repeat(60));
        
        // 4. SCENARIO: Test the application limit (Attempt 4)
        
        Application app4 = applicationManager.apply(student.getID(), int0005.getInternshipID());
        boolean success4 = student.applyForInternship(app4);
        System.out.printf("Attempt 4 (INT0005): %s\n", success4 ? "SUCCESS (RULE VIOLATION)" : "FAILURE (Limit Check Passed)");
        
        // 5. VERIFICATION
        
        int finalAppCount = student.getApplications().size();
        System.out.println("-".repeat(60));
        System.out.printf("Final number of applications for %s: %d\n", student.getID(), finalAppCount);

        if (finalAppCount == 3 && !success4) {
            System.out.println("Test Status: PASSED (Student application limit rule enforced correctly).");
        } else {
            System.out.println("Test Status: FAILED (Application limit rule was violated or incorrectly reported).");
        }
        System.out.println("=".repeat(60));
    }
}