package models;

import java.time.LocalDate;

public class Application {
    public enum ApplicationStatus {PENDING, SUCCESSFUL, UNSUCCESSFUL, WITHDRAWN, WITHDRAW_REQUESTED}

    private String id;
    private String studentID; 
    private String internshipID; 
    private ApplicationStatus status;
    private String appliedDate; 

    private transient Student studentRef;
    private transient Internship internshipRef;

    // Full constructor
    public Application(String id, String studentID, String internshipID, ApplicationStatus status, String appliedDate) {
        this.id = id;
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.status = status == null ? ApplicationStatus.PENDING : status;
        this.appliedDate = appliedDate == null ? LocalDate.now().toString() : appliedDate;
    }

    // Convenience constructor used by some serializers (id, studentId, internshipId, statusAsString)
    public Application(String id, String studentID, String internshipID, String status) {
        this.id = id;
        this.studentID = studentID;
        this.internshipID = internshipID;
        try {
            this.status = ApplicationStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.status = ApplicationStatus.PENDING;
        }
        this.appliedDate = LocalDate.now().toString();
    }

    // Simple constructor for apply() when student and internship objects are available
    public Application(String studentID, String internshipID) {
        this.id = "APP" + System.currentTimeMillis();
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.status = ApplicationStatus.PENDING;
        this.appliedDate = LocalDate.now().toString();
        this.studentRef = null; // Explicitly set to null
        this.internshipRef = null; // Explicitly set to null
    }

    public String getID() { return id; }
    public String getStudentID() { return studentID; }
    public String getInternshipID() { return internshipID; }
    public ApplicationStatus getStatus() { return status; }
    public String getAppliedDate() { return appliedDate; }

    public void setStatus(ApplicationStatus status) { this.status = status; }
    public void setStudentID(String s) { this.studentID = s; }
    public void setInternshipID(String i) { this.internshipID = i; }

    public Student getStudent() { return studentRef; }
    public Internship getInternship() { return internshipRef; }

    public void setStudentRef(Student studentRef) { this.studentRef = studentRef; }
    public void setInternshipRef(Internship internshipRef) { this.internshipRef = internshipRef; }

    @Override
    public String toString() {
        String internInfo = (internshipID == null) ? "<unknown internship>" : internshipID;
        String studentInfo = (studentID == null) ? "<unknown student>" : studentID;
        return String.format("Application[id=%s, student=%s, internship=%s, status=%s, date=%s]",
                id, studentInfo, internInfo, status, appliedDate);
    }
}
