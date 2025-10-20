package main;

import java.util.ArrayList;
import managers.UserManager;
import models.User;
import models.Student;
import models.InternshipApplication;

public class Main {
    public static void main(String[] args) {
        // --- Create sample student data ---
        Student s1 = new Student("U001", "Alice Tan", "alice@uni.edu", 2, "CSC");
        Student s2 = new Student("U002", "Bob Lee", "bob@uni.edu", 4, "EEE");

        ArrayList<User> studentList = new ArrayList<>();
        studentList.add(s1);
        studentList.add(s2);

        ArrayList<User> repList = new ArrayList<>();
        ArrayList<User> staffList = new ArrayList<>();

        // --- Initialize UserManager ---
        UserManager um = new UserManager(studentList, repList, staffList);

        // --- Test login ---
        System.out.println("\n--- LOGIN TEST ---");
        um.login("U001", "password");  // Default password from User.java

        // --- Apply for internships ---
        Student currentStudent = (Student) um.getCurrentUser();

        InternshipApplication i1 = new InternshipApplication("I001", "Software Intern", "Basic");
        InternshipApplication i2 = new InternshipApplication("I002", "AI Research Intern", "Advanced");

        System.out.println("\n--- APPLY TEST ---");
        currentStudent.applyForInternship(i1);  // Should succeed (Basic)
        currentStudent.applyForInternship(i2);  // Should fail (Year 2 can't apply for Advanced)

        // --- View applications ---
        System.out.println("\n--- VIEW APPLICATIONS ---");
        currentStudent.viewApplications();

        // --- Simulate company success and acceptance ---
        i1.setStatus("Successful");
        System.out.println("\n--- ACCEPT INTERNSHIP ---");
        currentStudent.acceptInternship("I001");

        // --- Request withdrawal (for demonstration) ---
        System.out.println("\n--- WITHDRAWAL REQUEST ---");
        currentStudent.requestWithdrawal("I001");

        // --- Logout ---
        System.out.println("\n--- LOGOUT TEST ---");
        um.logout();
    }
}
