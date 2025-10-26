package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import models.Internship;
import models.Internship.InternshipLevel;
import models.Internship.InternshipStatus;

public class InternshipFileHandler{
    private String filePath = "assets/testcases/internship_list.csv";
    private ArrayList<Internship> internshipList = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Read internships from file
     * Format: InternshipID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,
     *         Status,CompanyName,CompanyRepId,TotalSlots,AvailableSlots,ConfirmedSlots,Visible
     */
    public ArrayList<Internship> readFromFile() {
        internshipList.clear();
        try (Scanner sc = new Scanner(new File(filePath))) {
            // Skip header if exists
            if (sc.hasNextLine()) {
                String firstLine = sc.nextLine();
                // Check if it's a header line
                if (!firstLine.startsWith("INT")) {
                    // It's a header, continue reading
                } else {
                    // First line is data, process it
                    processLine(firstLine);
                }
            }
            
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                processLine(line);
            }
            
            System.out.println("Successfully loaded " + internshipList.size() + " internships from file.");
            
        } catch (FileNotFoundException e) {
            System.out.println("Internship file not found. Starting with empty list.");
        } catch (Exception e) {
            System.err.println("Error reading internship file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return internshipList;
    }
    
    /**
     * Process a single line from the file
     */
    private void processLine(String line) {
        try {
            String[] rowData = line.split(",");
            
            if (rowData.length >= 14) {
                String internshipId = rowData[0].trim();
                String title = rowData[1].trim();
                String description = rowData[2].trim();
                InternshipLevel level = InternshipLevel.valueOf(rowData[3].trim());
                String preferredMajor = rowData[4].trim();
                LocalDate openingDate = LocalDate.parse(rowData[5].trim(), DATE_FORMATTER);
                LocalDate closingDate = LocalDate.parse(rowData[6].trim(), DATE_FORMATTER);
                InternshipStatus status = InternshipStatus.valueOf(rowData[7].trim());
                String companyName = rowData[8].trim();
                String companyRepId = rowData[9].trim();
                int totalSlots = Integer.parseInt(rowData[10].trim());
                int availableSlots = Integer.parseInt(rowData[11].trim());
                int confirmedSlots = Integer.parseInt(rowData[12].trim());
                boolean visible = Boolean.parseBoolean(rowData[13].trim());
                
                Internship internship = new Internship(
                    internshipId, title, description, level, preferredMajor,
                    openingDate, closingDate, status, companyName, companyRepId,
                    totalSlots, availableSlots, confirmedSlots, visible
                );
                
                internshipList.add(internship);
            }
        } catch (Exception e) {
            System.err.println("Error processing line: " + line);
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Write all internships to file
     */
    public void writeToFile(ArrayList<Internship> internshipList) {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            // Write header
            pw.println("InternshipID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,CompanyRepId,TotalSlots,AvailableSlots,ConfirmedSlots,Visible");
            
            // Write each internship
            for (Internship internship : internshipList) {
                pw.println(formatInternshipToCSV(internship));
            }
            
            System.out.println("Successfully saved " + internshipList.size() + " internships to file.");
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Could not write to file: " + e.getMessage());
        }
    }
    
    /**
     * Format internship object to CSV format
     */
    private String formatInternshipToCSV(Internship internship) {
        return String.join(",",
            internship.getInternshipId(),
            internship.getTitle(),
            internship.getDescription(),
            internship.getLevel().name(),
            internship.getPreferredMajor(),
            internship.getOpeningDate().format(DATE_FORMATTER),
            internship.getClosingDate().format(DATE_FORMATTER),
            internship.getStatus().name(),
            internship.getCompanyName(),
            internship.getCompanyRepId(),
            String.valueOf(internship.getTotalSlots()),
            String.valueOf(internship.getAvailableSlots()),
            String.valueOf(internship.getConfirmedSlots()),
            String.valueOf(internship.isVisible())
        );
    }
    
    /**
     * Add a single internship to file (append mode)
     */
    public void addToFile(Internship internship) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            pw.println(formatInternshipToCSV(internship));
            System.out.println("Internship added to file: " + internship.getInternshipId());
            
        } catch (IOException e) {
            System.err.println("Error adding internship to file: " + e.getMessage());
        }
    }
    
    /**
     * Remove an internship from file by ID
     */
    public void removeFromFile(String internshipId) {
        internshipList = this.readFromFile();
        boolean removed = false;
        
        for (int i = 0; i < internshipList.size(); i++) {
            if (internshipList.get(i).getInternshipId().equals(internshipId)) {
                internshipList.remove(i);
                removed = true;
                break;
            }
        }
        
        if (removed) {
            this.writeToFile(internshipList);
            System.out.println("Internship removed: " + internshipId);
        } else {
            System.out.println("Internship not found: " + internshipId);
        }
    }
    
    /**
     * Update an existing internship in the file
     */
    public void updateInFile(Internship updatedInternship) {
        internshipList = this.readFromFile();
        boolean updated = false;
        
        for (int i = 0; i < internshipList.size(); i++) {
            if (internshipList.get(i).getInternshipId().equals(updatedInternship.getInternshipId())) {
                internshipList.set(i, updatedInternship);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            this.writeToFile(internshipList);
            System.out.println("Internship updated: " + updatedInternship.getInternshipId());
        } else {
            System.out.println("Internship not found for update: " + updatedInternship.getInternshipId());
        }
    }
    
    /**
     * Get the file path
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * Set a custom file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}