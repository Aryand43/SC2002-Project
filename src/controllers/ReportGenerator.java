package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import managers.ApplicationManager;
import managers.InternshipManager;
import managers.UserManager;
import models.Application;
import models.CareerCenterStaff;
import models.CompanyRepresentative;
import models.Internship;
import models.Student;

public class ReportGenerator {
    
    private ApplicationManager applicationManager;
    private InternshipManager internshipManager;
    private UserManager userManager;
    
    public ReportGenerator(ApplicationManager applicationManager, 
                          InternshipManager internshipManager, 
                          UserManager userManager) {
        this.applicationManager = applicationManager;
        this.internshipManager = internshipManager;
        this.userManager = userManager;
    }
    
    /**
     * Generates a report of internships filtered by their status.
     * Status can be: PENDING, APPROVED, REJECTED, FILLED
     *
     * @param status the internship status to filter by
     * @return list of internships matching the specified status
     */
    public List<Internship> generateReportByStatus(String status) {
        if (status == null || status.isEmpty()) {
            return new ArrayList<>();
        }
        Predicate<Internship> predicate = i -> i.getStatus().toString().equalsIgnoreCase(status);
        return filterInternships(predicate, list -> System.out.println("Found " + list.size() + " internships with status: " + status));
    }
    
        /**
     * Generate internships filtered by preferred major (case-insensitive).
     * @param preferredMajor major to filter by
     * @return list of internships matching preferred major
     */
    public List<Internship> generateReportByPreferredMajor(String preferredMajor) {
        if (preferredMajor == null || preferredMajor.isEmpty()) {
            return new ArrayList<>();
        }
        Predicate<Internship> predicate = i -> i.getPreferredMajor() != null && i.getPreferredMajor().equalsIgnoreCase(preferredMajor);
        return filterInternships(predicate, list -> System.out.println("Found " + list.size() + " internships for preferred major: " + preferredMajor));
    }

    /**
     * Generate internships filtered by level. Level string is matched against Internship.InternshipLevel enum.
     * @param levelStr level name (BASIC, INTERMEDIATE, ADVANCED)
     * @return list of internships matching the level
     */
    public List<Internship> generateReportByLevel(String levelStr) {
        if (levelStr == null || levelStr.isEmpty()) {
            return new ArrayList<>();
        }
        Predicate<Internship> predicate = i -> i.getLevel() != null && i.getLevel().equalsIgnoreCase(levelStr);
        return filterInternships(predicate, list -> System.out.println("Found " + list.size() + " internships from level: " + levelStr));
    }

    /**
     * Generates a report of internships filtered by company name.
     *
     * @param companyName the company name to filter by
     * @return list of internships from the specified company
     */
    public List<Internship> generateReportByCompany(String companyName) {
        if (companyName == null || companyName.isEmpty()) {
            return new ArrayList<>();
        }
        Predicate<Internship> predicate = i -> i.getCompanyName() != null && i.getCompanyName().equalsIgnoreCase(companyName);
        return filterInternships(predicate, list -> System.out.println("Found " + list.size() + " internships from company: " + companyName));
    }
    
    /**
     * Generates a report of applications for a specific student.
     *
     * @param studentId the student ID to generate report for
     * @return list of applications for the specified student
     */
    public List<Application> generateReportByStudent(String studentId) {
        System.out.println("Generating application report for student: " + studentId);
        
    // Get all applications and filter by student
    List<Application> allApplications = applicationManager.getApplicationList();
        List<Application> studentApplications = allApplications.stream()
                .filter(app -> app.getStudent() != null && app.getStudent().getID().equals(studentId))
                .collect(Collectors.toList());

        System.out.println("Found " + studentApplications.size() + " applications for student: " + studentId);
        studentApplications.forEach(application -> {
            Internship internship = application.getInternship();
            String title = internship != null ? internship.getTitle() : "<unknown>";
            System.out.println("- Internship: " + title + " | Status: " + application.getStatus() + " | Applied: " + application.getAppliedDate());
        });
        return studentApplications;
    }
    
    /**
     * Generates a comprehensive summary report of the entire internship system.
     * This includes statistics on internships, applications, and users.
     *
     * @return a string containing the summary report
     */
    public String generateSummaryReport() {
        System.out.println("Generating comprehensive summary report...");
        
        StringBuilder report = new StringBuilder();

        report.append("=== INTERNSHIP PLACEMENT SYSTEM SUMMARY REPORT ===\n\n");

        report.append("USER STATISTICS:\n");
        report.append("----------------\n");
        
        List<Student> students = userManager.getStudentList();
        List<CompanyRepresentative> companyReps = userManager.getRepList();
        List<CareerCenterStaff> staff = userManager.getStaffList();
        
        report.append("Total Students: ").append(students.size()).append("\n");
        report.append("Total Company Representatives: ").append(companyReps.size()).append("\n");
        
        long approvedReps = companyReps.stream()
            .filter(rep -> rep.isApproved())
            .count();
        report.append("Approved Company Representatives: ").append(approvedReps).append("\n");
        
        report.append("Total Career Center Staff: ").append(staff.size()).append("\n\n");
        
        // Internship Statistics
        report.append("INTERNSHIP STATISTICS:\n");
        report.append("----------------------\n");
        
        List<Internship> allInternships = internshipManager.getAllInternships();
        report.append("Total Internship Opportunities: ").append(allInternships.size()).append("\n");
        
        // Count by status
        long pending = allInternships.stream()
            .filter(i -> i.getStatus() == Internship.InternshipStatus.PENDING)
            .count();
        long approved = allInternships.stream()
            .filter(i -> i.getStatus() == Internship.InternshipStatus.APPROVED)
            .count();
        long rejected = allInternships.stream()
            .filter(i -> i.getStatus() == Internship.InternshipStatus.REJECTED)
            .count();
        long filled = allInternships.stream()
            .filter(i -> i.getStatus() == Internship.InternshipStatus.FILLED)
            .count();
        
        report.append(" - PENDING: ").append(pending).append("\n");
        report.append(" - APPROVED: ").append(approved).append("\n");
        report.append(" - REJECTED: ").append(rejected).append("\n");
        report.append(" - FILLED: ").append(filled).append("\n");
        
        // Count by level
        long basic = allInternships.stream()
            .filter(i -> i.getLevel() == Internship.InternshipLevel.BASIC)
            .count();
        long intermediate = allInternships.stream()
            .filter(i -> i.getLevel() == Internship.InternshipLevel.INTERMEDIATE)
            .count();
        long advanced = allInternships.stream()
            .filter(i -> i.getLevel() == Internship.InternshipLevel.ADVANCED)
            .count();
        
        report.append("Internships by Level:\n");
        report.append(" - BASIC: ").append(basic).append("\n");
        report.append(" - INTERMEDIATE: ").append(intermediate).append("\n");
        report.append(" - ADVANCED: ").append(advanced).append("\n\n");
        
        // Application Statistics
        report.append("APPLICATION STATISTICS:\n");
        report.append("-----------------------\n");
        
    List<Application> allApplications = applicationManager.getApplicationList();
        report.append("Total Applications: ").append(allApplications.size()).append("\n");
        
        // Count by application status
        long appPending = allApplications.stream()
            .filter(app -> app.getStatus() == Application.ApplicationStatus.PENDING)
            .count();
        long appSuccessful = allApplications.stream()
            .filter(app -> app.getStatus() == Application.ApplicationStatus.SUCCESSFUL)
            .count();
        long appUnsuccessful = allApplications.stream()
            .filter(app -> app.getStatus() == Application.ApplicationStatus.UNSUCCESSFUL)
            .count();
        long appWithdrawn = allApplications.stream()
            .filter(app -> app.getStatus() == Application.ApplicationStatus.WITHDRAWN)
            .count();
        
        report.append(" - PENDING: ").append(appPending).append("\n");
        report.append(" - SUCCESSFUL: ").append(appSuccessful).append("\n");
        report.append(" - UNSUCCESSFUL: ").append(appUnsuccessful).append("\n");
        report.append(" - WITHDRAWN: ").append(appWithdrawn).append("\n\n");
        
        // Student Application Statistics
        report.append("STUDENT APPLICATION OVERVIEW:\n");
        report.append("------------------------------\n");
        
        long studentsWithApplications = students.stream()
            .filter(student -> !student.getApplications().isEmpty())
            .count();
        report.append("Students with at least one application: ").append(studentsWithApplications).append("\n");
        
        // Calculate average applications per student
        double avgApplications = allApplications.size() / (double) Math.max(students.size(), 1);
        report.append("Average applications per student: ").append(String.format("%.2f", avgApplications)).append("\n");
        
        // Find students with maximum applications
        Student maxAppStudent = null;
        int maxApplications = 0;
        for (Student student : students) {
            int appCount = student.getApplications().size();
            if (appCount > maxApplications) {
                maxApplications = appCount;
                maxAppStudent = student;
            }
        }
        
        if (maxAppStudent != null) {
            report.append("Student with most applications: ").append(maxAppStudent.getUserName())
                  .append(" (").append(maxApplications).append(" applications)\n");
        }
        
        System.out.println(report.toString());
        
        return report.toString();
    }
    
    /**
     * Generates a custom filtered report based on multiple criteria.
     * This is an enhanced method beyond the basic UML requirements.
     *
     * @param status the internship status filter (optional)
     * @param level the internship level filter (optional)
     * @param major the preferred major filter (optional)
     * @param company the company name filter (optional)
     * @return list of internships matching all specified criteria
     */
    public List<Internship> generateCustomReport(String status, String level, String major, String company) {
        System.out.println("Generating custom report with filters - Status: " + status + ", Level: " + level + ", Major: " + major + ", Company: " + company);

        Predicate<Internship> predicate = i -> {
            boolean matches = true;
            if (status != null && !status.isEmpty()) {
                matches = matches && i.getStatus() != null && i.getStatus().toString().equalsIgnoreCase(status);
            }
            if (level != null && !level.isEmpty()) {
                matches = matches && i.getLevel() != null && i.getLevel().toString().equalsIgnoreCase(level);
            }
            if (major != null && !major.isEmpty()) {
                matches = matches && i.getPreferredMajor() != null && i.getPreferredMajor().equalsIgnoreCase(major);
            }
            if (company != null && !company.isEmpty()) {
                matches = matches && i.getCompanyName() != null && i.getCompanyName().equalsIgnoreCase(company);
            }
            return matches;
        };

        Consumer<List<Internship>> printer = list -> System.out.println("Custom report found " + list.size() + " matching internships.");
        return filterInternships(predicate, printer);
    }

    /**
     * Helper that filters internships by a predicate and optionally prints a summary using the provided printer.
     * Keeps the public methods concise and uses streams internally for efficiency.
     */
    private List<Internship> filterInternships(Predicate<Internship> predicate, Consumer<List<Internship>> printer) {
        List<Internship> allInternships = internshipManager.getAllInternships();
        List<Internship> result = allInternships.stream()
                .filter(predicate)
                .collect(Collectors.toList());
        if (printer != null) {
            try {
                printer.accept(result);
            } catch (Exception ignored) {
                // Printing is best-effort; don't fail the query on printer errors
            }
        }
        return result;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
    
    public InternshipManager getInternshipManager() {
        return internshipManager;
    }
    
    public UserManager getUserManager() {
        return userManager;
    }
}
