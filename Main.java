import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import boundaries.*;
import managers.*;
import controllers.*;

public class Main {
    /**
     * Input an integer with validation
     */
    private int inputInteger(String prompt, int min, int max) {
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
    
    /**
     * Input a date with validation
     */
    private LocalDate inputDate(String prompt) {
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
    public static void main (String args[]){
        
        InternshipBoundary.displayInternshipList(internships);
        user = userManager.getcurrentuser

        switch(user){
            case TypesofUser.Student:
                printSectionHeader("Student Internship Menu");
                System.out.println("1. View Available Internships");
                System.out.println("2. Search Internships");
                System.out.println("3. Apply for Internship");
                System.out.println("4. View My Applications");
                System.out.println("5. Filter Internships");
                System.out.println("0. Back to Main Menu");

                inputInteger("Enter Option:", 0, 6);

        }
    }
}
