package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.Application;
import models.CareerCenterStaff;
import models.CompanyRepresentative;
import models.Internship;
import models.Student;

/**
 * <b>REPORT GENERATOR CLASS</b><br>
 * A ReportGenerator object provides comprehensive reporting capabilities for the internship management system.<br>
 * <br>
 * Key functionalities include:
 * <ul>
 * <li>Generate reports filtered by internship status</li>
 * <li>Generate reports filtered by preferred major</li>
 * <li>Generate reports filtered by internship level</li>
 * <li>Generate reports filtered by company name</li>
 * <li>Generate reports for a specific student's applications</li>
 * <li>Generate summary reports for the entire system</li>
 * <li>Generate custom reports with multiple filters</li>
 * </ul>
 * <br>
 * This class reuses existing manager APIs from {@link controllers.ApplicationManager}, 
 * {@link controllers.InternshipManager}, and {@link controllers.UserManager}.
 * 
 * @author Hanyue
 */
public class ReportGenerator {

    private final ApplicationManager applicationManager;
    private final InternshipManager internshipManager;
    private final UserManager userManager;

    /**
     * Class Constructor
     * @param applicationManager Sets the ApplicationManager instance for managing applications
     * @param internshipManager Sets the InternshipManager instance for managing internships
     * @param userManager Sets the UserManager instance for managing users
     */
    public ReportGenerator(ApplicationManager applicationManager,
                           InternshipManager internshipManager,
                           UserManager userManager) {
        this.applicationManager = applicationManager;
        this.internshipManager = internshipManager;
        this.userManager = userManager;
    }

    /**
     * Getter Function
     * @return ApplicationManager instance
     */
    public ApplicationManager getApplicationManager() { return applicationManager; }
    
    /**
     * Getter Function
     * @return InternshipManager instance
     */
    public InternshipManager getInternshipManager() { return internshipManager; }
    
    /**
     * Getter Function
     * @return UserManager instance
     */
    public UserManager getUserManager() { return userManager; }
    
    /**
     * Generates a report of internships filtered by status<br>
     * @param status The internship status to filter by (e.g., "PENDING", "APPROVED", "REJECTED", "FILLED")
     * @return List of {@link models.Internship} objects matching the specified status<br>
     * Returns an empty list if status is null, empty, or invalid
     */
    public List<Internship> generateReportByStatus(String status) {
        if (status == null || status.isEmpty()) return new ArrayList<>();
        try {
            Internship.InternshipStatus st = Internship.InternshipStatus.valueOf(status.toUpperCase());
            return internshipManager.filterInternships(st, null, null, null);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Generates a report of internships filtered by preferred major<br>
     * @param preferredMajor The preferred major to filter internships by
     * @return List of {@link models.Internship} objects matching the specified preferred major<br>
     * Returns an empty list if preferredMajor is null or empty
     */
    public List<Internship> generateReportByPreferredMajor(String preferredMajor) {
        if (preferredMajor == null || preferredMajor.isEmpty()) return new ArrayList<>();
        return internshipManager.filterInternships(null, null, preferredMajor, null);
    }

    /**
     * Generates a report of internships filtered by internship level<br>
     * @param levelStr The internship level to filter by (BASIC, INTERMEDIATE, ADVANCED)
     * @return List of {@link models.Internship} objects matching the specified level<br>
     * Returns an empty list if levelStr is null, empty, or invalid
     */
    public List<Internship> generateReportByLevel(String levelStr) {
        if (levelStr == null || levelStr.isEmpty()) return new ArrayList<>();
        try {
            Internship.InternshipLevel lvl = Internship.InternshipLevel.valueOf(levelStr.toUpperCase());
            return internshipManager.filterInternships(null, lvl, null, null);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Generates a report of internships filtered by company name<br>
     * @param companyName The company name to filter internships by
     * @return List of {@link models.Internship} objects matching the specified company<br>
     * Returns an empty list if companyName is null or empty
     */
    public List<Internship> generateReportByCompany(String companyName) {
        if (companyName == null || companyName.isEmpty()) return new ArrayList<>();
        return internshipManager.filterInternships(null, null, null, companyName);
    }

    /**
     * Generates a report of all applications for a specific student<br>
     * @param studentId The student ID to generate the report for
     * @return List of {@link models.Application} objects submitted by the specified student<br>
     * Returns an empty list if no applications are found for the student
     */
    public List<Application> generateReportByStudent(String studentId) {
        List<Application> allApplications = applicationManager.getApplicationList();
        return allApplications.stream()
                .filter(app -> app.getStudent() != null && studentId.equals(app.getStudent().getID()))
                .collect(Collectors.toList());
    }

    /**
     * Generates a comprehensive summary report of the entire internship placement system<br>
     * <br>
     * The report includes:
     * <ul>
     * <li>Total number of students</li>
     * <li>Total number of company representatives (approved and pending)</li>
     * <li>Total number of career center staff</li>
     * <li>Total internship opportunities (broken down by status)</li>
     * <li>Total applications (broken down by status)</li>
     * </ul>
     * @return A formatted string containing the complete summary report
     */
    public String generateSummaryReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== INTERNSHIP PLACEMENT SYSTEM SUMMARY REPORT ===\\n\\n");

        List<Student> students = userManager.getStudentList();
        List<CompanyRepresentative> companyReps = userManager.getRepList();
        List<CareerCenterStaff> staff = userManager.getStaffList();

        report.append("Total Students: ").append(students.size()).append("\\n");
        report.append("Total Company Representatives: ").append(companyReps.size()).append("\\n");
        report.append("Approved Company Representatives: ")
                .append(companyReps.stream().filter(CompanyRepresentative::isApproved).count()).append("\\n");
        report.append("Total Career Center Staff: ").append(staff.size()).append("\\n\\n");

        List<Internship> allInternships = internshipManager.getAllInternships();
        report.append("Total Internship Opportunities: ").append(allInternships.size()).append("\\n");

        report.append(" - PENDING: ").append(allInternships.stream().filter(i -> i.getStatus() == Internship.InternshipStatus.PENDING).count()).append("\\n");
        report.append(" - APPROVED: ").append(allInternships.stream().filter(i -> i.getStatus() == Internship.InternshipStatus.APPROVED).count()).append("\\n");
        report.append(" - REJECTED: ").append(allInternships.stream().filter(i -> i.getStatus() == Internship.InternshipStatus.REJECTED).count()).append("\\n");
        report.append(" - FILLED: ").append(allInternships.stream().filter(i -> i.getStatus() == Internship.InternshipStatus.FILLED).count()).append("\\n\\n");

        List<Application> allApplications = applicationManager.getApplicationList();
        report.append("Total Applications: ").append(allApplications.size()).append("\\n");
        report.append(" - PENDING: ").append(allApplications.stream().filter(app -> app.getStatus() == Application.ApplicationStatus.PENDING).count()).append("\\n");
        report.append(" - SUCCESSFUL: ").append(allApplications.stream().filter(app -> app.getStatus() == Application.ApplicationStatus.SUCCESSFUL).count()).append("\\n");
        report.append(" - UNSUCCESSFUL: ").append(allApplications.stream().filter(app -> app.getStatus() == Application.ApplicationStatus.UNSUCCESSFUL).count()).append("\\n");
        report.append(" - WITHDRAWN: ").append(allApplications.stream().filter(app -> app.getStatus() == Application.ApplicationStatus.WITHDRAWN).count()).append("\\n\\n");

        return report.toString();
    }

    /**
     * Generates a custom report with multiple filter criteria<br>
     * All parameters are optional and can be combined for more specific results.
     * @param status The internship status to filter by (optional, can be null or empty)
     * @param level The internship level to filter by (optional, can be null or empty)
     * @param major The preferred major to filter by (optional, can be null or empty)
     * @param company The company name to filter by (optional, can be null or empty)
     * @return List of {@link models.Internship} objects matching all specified criteria<br>
     * Returns all internships if no valid filters are provided
     */
    public List<Internship> generateCustomReport(String status, String level, String major, String company) {
        Internship.InternshipStatus st = null;
        Internship.InternshipLevel lvl = null;
        try { if (status != null && !status.isEmpty()) st = Internship.InternshipStatus.valueOf(status.toUpperCase()); } catch (IllegalArgumentException ignored) {}
        try { if (level != null && !level.isEmpty()) lvl = Internship.InternshipLevel.valueOf(level.toUpperCase()); } catch (IllegalArgumentException ignored) {}
        String maj = (major != null && !major.isEmpty()) ? major : null;
        String comp = (company != null && !company.isEmpty()) ? company : null;
        return internshipManager.filterInternships(st, lvl, maj, comp);
    }
}
