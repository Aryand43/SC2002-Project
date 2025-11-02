package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import managers.ApplicationManager;
import managers.InternshipManager;
import managers.UserManager;
import models.Application;
import models.CareerCenterStaff;
import models.CompanyRepresentative;
import models.Internship;
import models.Student;

/**
 * Clean ReportGenerator implementation that reuses existing manager APIs.
 */
public class ReportGenerator {

    private final ApplicationManager applicationManager;
    private final InternshipManager internshipManager;
    private final UserManager userManager;

    public ReportGenerator(ApplicationManager applicationManager,
                           InternshipManager internshipManager,
                           UserManager userManager) {
        this.applicationManager = applicationManager;
        this.internshipManager = internshipManager;
        this.userManager = userManager;
    }

    public List<Internship> generateReportByStatus(String status) {
        if (status == null || status.isEmpty()) return new ArrayList<>();
        try {
            Internship.InternshipStatus st = Internship.InternshipStatus.valueOf(status.toUpperCase());
            return internshipManager.filterInternships(st, null, null, null);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    public List<Internship> generateReportByPreferredMajor(String preferredMajor) {
        if (preferredMajor == null || preferredMajor.isEmpty()) return new ArrayList<>();
        return internshipManager.filterInternships(null, null, preferredMajor, null);
    }

    public List<Internship> generateReportByLevel(String levelStr) {
        if (levelStr == null || levelStr.isEmpty()) return new ArrayList<>();
        try {
            Internship.InternshipLevel lvl = Internship.InternshipLevel.valueOf(levelStr.toUpperCase());
            return internshipManager.filterInternships(null, lvl, null, null);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    public List<Internship> generateReportByCompany(String companyName) {
        if (companyName == null || companyName.isEmpty()) return new ArrayList<>();
        return internshipManager.filterInternships(null, null, null, companyName);
    }

    public List<Application> generateReportByStudent(String studentId) {
        List<Application> allApplications = applicationManager.getApplicationList();
        return allApplications.stream()
                .filter(app -> app.getStudent() != null && studentId.equals(app.getStudent().getID()))
                .collect(Collectors.toList());
    }

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

    public List<Internship> generateCustomReport(String status, String level, String major, String company) {
        Internship.InternshipStatus st = null;
        Internship.InternshipLevel lvl = null;
        try { if (status != null && !status.isEmpty()) st = Internship.InternshipStatus.valueOf(status.toUpperCase()); } catch (IllegalArgumentException ignored) {}
        try { if (level != null && !level.isEmpty()) lvl = Internship.InternshipLevel.valueOf(level.toUpperCase()); } catch (IllegalArgumentException ignored) {}
        String maj = (major != null && !major.isEmpty()) ? major : null;
        String comp = (company != null && !company.isEmpty()) ? company : null;
        return internshipManager.filterInternships(st, lvl, maj, comp);
    }

    public ApplicationManager getApplicationManager() { return applicationManager; }
    public InternshipManager getInternshipManager() { return internshipManager; }
    public UserManager getUserManager() { return userManager; }
}
