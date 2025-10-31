package models;

import java.time.LocalDate;

public class Application {
    public enum ApplicationStatus {PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN, WITHDRAW_REQUESTED}

    private String id;
    private Student student; // may be null if constructed from file with string ids
    private Internship internship; // may be null if constructed from file with string ids
    private ApplicationStatus status;
    private String appliedDate; 

    // Full constructor
    public Application(String id, Student student, Internship internship, ApplicationStatus status, String appliedDate) {
        this.id = id;
        this.student = student;
        this.internship = internship;
        this.status = status == null ? ApplicationStatus.PENDING : status;
        this.appliedDate = appliedDate == null ? LocalDate.now().toString() : appliedDate;
    }

    // Convenience constructor used by some serializers (id, studentId, internshipId, statusAsString)
    public Application(String id, String studentId, String internshipId, String status) {
        this.id = id;
        this.student = null;
        this.internship = null;
        try {
            this.status = ApplicationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.status = ApplicationStatus.PENDING;
        }
        this.appliedDate = LocalDate.now().toString();
    }

    // Simple constructor for apply() when student and internship objects are available
    public Application(Student student, Internship internship) {
        this.id = "APP" + System.currentTimeMillis();
        this.student = student;
        this.internship = internship;
        this.status = ApplicationStatus.PENDING;
        this.appliedDate = LocalDate.now().toString();
    }

    public String getID() { return id; }
    public Student getStudent() { return student; }
    public Internship getInternship() { return internship; }
    public ApplicationStatus getStatus() { return status; }
    public String getAppliedDate() { return appliedDate; }

    public void setStatus(ApplicationStatus status) { this.status = status; }
    public void setStudent(Student s) { this.student = s; }
    public void setInternship(Internship i) { this.internship = i; }

    public String toString() {
        String internInfo = (internship == null) ? "<unknown internship>" : internship.getTitle();
        String studentInfo = (student == null) ? "<unknown student>" : student.getUserName();
        return String.format("Application[id=%s, student=%s, internship=%s, status=%s, date=%s]",
                id, studentInfo, internInfo, status, appliedDate);
    }
}
